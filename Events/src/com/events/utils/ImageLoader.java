package com.events.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Thread.State;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Handler;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageLoader.
 */
public class ImageLoader extends ImageUtils
{
	
	/** The Constant MAX_BM_SIZE. */
	private static final long MAX_BM_SIZE = 1024 * 5;
	
	/** The Cache. */
	private final HashMap<String, SoftReference<Bitmap>> Cache = new HashMap<String, SoftReference<Bitmap>>();

	/** The w. */
	private int w;
	
	/** The h. */
	private int h;
	
	/** The scale. */
	private int scale = ImageUtils.SCALE_ASPECT;
	
	/** The local. */
	private boolean local;
	
	/** The save in main thread. */
	public boolean saveInMainThread = false;

	/**
	 * The Class QueueItem.
	 */
	public final class QueueItem
	{
		
		/** The url. */
		public String url;
		
		/** The listener. */
		public ImageLoadedListener listener;
	}

	/** The Queue. */
	private final ArrayList<QueueItem> Queue = new ArrayList<QueueItem>();
	
	/** The handler. */
	private final Handler handler = new Handler();
	
	/** The thread. */
	private Thread thread;
	
	/** The runner. */
	private QueueRunner runner = new QueueRunner();

	/**
	 * Instantiates a new image loader.
	 *
	 * @param w the w
	 * @param h the h
	 */
	public ImageLoader(int w, int h)
	{

		this.w = w;
		this.h = h;
		thread = new Thread(runner);

	}

	/**
	 * Instantiates a new image loader.
	 *
	 * @param w the w
	 * @param h the h
	 * @param scale the scale
	 */
	public ImageLoader(int w, int h, int scale)
	{

		this(w, h);
		this.scale = scale;
	}

	/**
	 * Instantiates a new image loader.
	 *
	 * @param w the w
	 * @param h the h
	 * @param scale the scale
	 * @param local the local
	 */
	public ImageLoader(int w, int h, int scale, boolean local)
	{

		this(w, h, scale);
		this.local = local;
	}

	/**
	 * Clear.
	 */
	public void clear()
	{
		Queue.clear();
		Cache.clear();
	}

	/**
	 * Sets the local.
	 *
	 * @param local the new local
	 */
	public void setLocal(boolean local)
	{
		this.local = local;
	}

	/**
	 * The listener interface for receiving imageLoaded events.
	 * The class that is interested in processing a imageLoaded
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addImageLoadedListener<code> method. When
	 * the imageLoaded event occurs, that object's appropriate
	 * method is invoked.
	 *
	 //* @see ImageLoadedEvent
	 */
	public interface ImageLoadedListener
	{
		
		/**
		 * Image loaded.
		 *
		 * @param bm the bm
		 */
		public void imageLoaded(Bitmap bm);
	}

	/**
	 * The Class QueueRunner.
	 */
	public class QueueRunner implements Runnable
	{
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{

			synchronized (this)
			{
				while (Queue.size() > 0)
				{
					final QueueItem item = Queue.remove(0);

					if (Cache.get(item.url) != null
							&& Cache.get(item.url).get() != null
							&& !Cache.get(item.url).get().isRecycled())
					{
						handler.post(new Runnable() {
							@Override
							public void run()
							{

								try
								{
									if (item.listener != null)
									{
										SoftReference<Bitmap> ref = Cache
												.get(item.url.toString());
										item.listener.imageLoaded(ref.get());
									}
								} catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						});
					}
					else
					{
						Bitmap bmp = null;
						if (local)
							bmp = ImageUtils.getCompressedBm(
									new File(item.url), w, h, scale);

						if (bmp == null)
							bmp = readBitmapFromCache(item.url.toString(),
									false);
						if (bmp == null && Utils.isOnline())
							bmp = readBitmapFromNetwork(item.url);
						if (bmp != null)
							Cache.put(item.url.toString(),
									new SoftReference<Bitmap>(bmp));
						final Bitmap bm = bmp;
						handler.post(new Runnable() {
							@Override
							public void run()
							{

								if (item.listener != null)
								{
									item.listener.imageLoaded(bm);
								}
							}
						});
					}

				}
			}
		}
	}

	/**
	 * Load image.
	 *
	 * @param uri the uri
	 * @param listener the listener
	 * @return the bitmap
	 */
	public Bitmap loadImage(final String uri, final ImageLoadedListener listener)

	{

		if (Cache.containsKey(uri))
		{
			SoftReference<Bitmap> ref = Cache.get(uri);
			if (ref != null && ref.get() != null && !ref.get().isRecycled())
			{
				return ref.get();
			}
		}

		QueueItem item = new QueueItem();
		item.url = uri + "";
		item.listener = listener;
		if (getImageFile(uri).exists())
			Queue.add(0, item);
		else
			Queue.add(item);

		if (thread.getState() == State.NEW)
		{
			thread.start();
		}
		else if (thread.getState() == State.TERMINATED)
		{
			thread = new Thread(runner);
			thread.start();
		}
		return null;
	}

	/**
	 * Read bitmap from cache.
	 *
	 * @param file the file
	 * @param compress the compress
	 * @return the bitmap
	 */
	public Bitmap readBitmapFromCache(String file, boolean compress)
	{

		Bitmap bmp = null;
		try
		{
			File f = getImageFile(file);

			if (!f.exists())
				return bmp;
			if (!compress || w == 0 && h == 0)
				bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
			else
				bmp = ImageUtils.getCompressedBm(f, w, h, scale);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return bmp;
	}

	/**
	 * Write bitmap to cache.
	 *
	 * @param bm the bm
	 * @param url the url
	 */
	public void writeBitmapToCache(final Bitmap bm, String url)
	{

		final File f = getImageFile(url);

		if (saveInMainThread)
		{
			try
			{
				bm.compress(CompressFormat.PNG, 100, new FileOutputStream(f));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run()
			{

				try
				{
					bm.compress(CompressFormat.PNG, 100,
							new FileOutputStream(f));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Read bitmap from network.
	 *
	 * @param urlStr the url str
	 * @return the bitmap
	 */
	private Bitmap readBitmapFromNetwork(String urlStr)
	{

		if (!URLUtil.isValidUrl(urlStr))
			return null;
		String str;
		if (w > 0 && h > 0)
			str = urlStr.replace("w=&h=", "w=" + w + "&h=" + h);
		else
			str = urlStr;

		InputStream is = null;
		BufferedInputStream bis = null;
		Bitmap bmp = null;
		try
		{
			URL url = new URL(str.replaceAll(" ", "%20"));
			URLConnection conn = url.openConnection();
			conn.connect();

			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			int len = 0;
			while ((len = bis.read(buffer)) != -1)
			{
				out.write(buffer, 0, len);
			}

			buf = out.toByteArray();
			if (w == 0 && h == 0)
			{
				if (buf.length > MAX_BM_SIZE)
				{
					Options opt = new Options();
					opt.inScaled = true;
					opt.inDither = true;
					opt.inSampleSize = 2;
					opt.inTempStorage = new byte[16000];
					bmp = BitmapFactory
							.decodeByteArray(buf, 0, buf.length, opt);
				}
				else
					bmp = BitmapFactory.decodeByteArray(buf, 0, buf.length);
			}
			else
				bmp = ImageUtils.getCompressedBm(buf, w, h, scale);

			if (bmp != null)
				writeBitmapToCache(bmp, urlStr);

			out.close();
			if (is != null)
				is.close();
			if (bis != null)
				bis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return bmp;
	}

	/**
	 * Load single image.
	 *
	 * @param url the url
	 * @param adapter the adapter
	 * @return the bitmap
	 */
	public Bitmap loadSingleImage(final String url, final BaseAdapter adapter)
	{

		if (Cache.get(url) != null && Cache.get(url).get() != null
				&& !Cache.get(url).get().isRecycled())
			return Cache.get(url).get();
		else
		{
			new Thread(new Runnable() {
				@Override
				public void run()
				{

					Bitmap bmp = readBitmapFromCache(url, false);
					if (bmp == null && Utils.isOnline())
						bmp = readBitmapFromNetwork(url);
					if (bmp != null)
						Cache.put(url, new SoftReference<Bitmap>(bmp));
					handler.post(new Runnable() {

						@Override
						public void run()
						{

							if (Cache.get(url) != null
									&& Cache.get(url).get() != null
									&& !Cache.get(url).get().isRecycled())
								adapter.notifyDataSetChanged();
						}
					});
				}
			}).start();
			return null;
		}
	}

	/**
	 * Load single image bm.
	 *
	 * @param path the path
	 * @param v the v
	 * @param defImg the def img
	 */
	public void loadSingleImageBm(final String path, final ImageView v,
			final int defImg)
	{

		if (Cache.get(path) != null && Cache.get(path).get() != null
				&& !Cache.get(path).get().isRecycled())
			v.setImageBitmap(Cache.get(path).get());
		else
		{
			new Thread(new Runnable() {
				@Override
				public void run()
				{

					Bitmap bmp = readBitmapFromCache(path, false);
					if (bmp == null && Utils.isOnline())
						bmp = readBitmapFromNetwork(path);
					if (bmp != null)
						Cache.put(path, new SoftReference<Bitmap>(bmp));
					final Bitmap bm = bmp;
					handler.post(new Runnable() {

						@Override
						public void run()
						{

							if (bm != null)
								v.setImageBitmap(bm);
							else
								v.setImageResource(defImg);
						}
					});
				}
			}).start();
		}
	}

	/**
	 * Load bitmap.
	 *
	 * @param path the path
	 * @return the bitmap
	 */
	public Bitmap loadBitmap(final String path)
	{

		if (Cache.get(path) != null && Cache.get(path).get() != null
				&& !Cache.get(path).get().isRecycled())
			return Cache.get(path).get();
		else
		{
			Bitmap bmp = readBitmapFromCache(path, false);
			if (bmp == null && Utils.isOnline())
				bmp = readBitmapFromNetwork(path);
			if (bmp != null)
				Cache.put(path, new SoftReference<Bitmap>(bmp));
			return bmp;
		}
	}

	/**
	 * Gets the image file.
	 *
	 * @param file the file
	 * @return the image file
	 */
	public File getImageFile(String file)
	{
		if (file.length() > 200)
			file = file.substring(0, 200);
		File f = new File(Const.CACHE_DIR, w + "_" + h + "_" + "_" + scale
				+ Uri.encode(file));
		return f;
	}

}
