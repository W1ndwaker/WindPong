package net.windwaker.pong.resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.windwaker.pong.exception.resource.LoaderNotFoundException;

/**
 * Resource management.
 */
public class ResourceManager {
	private final Set<ResourceLoader<?>> loaders = new HashSet<>();

	/**
	 * Returns all registered loaders.
	 * @return registered loaders.
	 */
	public Set<ResourceLoader<?>> getLoaders() {
		return Collections.unmodifiableSet(loaders);
	}

	/**
	 * Returns the loader with the specified scheme.
	 * @param scheme to get
	 * @return loader scheme
	 */
	public ResourceLoader<?> getLoader(String scheme) {
		for (ResourceLoader<?> loader : loaders) {
			if (loader.getScheme().equals(scheme)) {
				return loader;
			}
		}
		return null;
	}

	/**
	 * Registers the loader.
	 * @param loader to register
	 */
	public void registerLoader(ResourceLoader<?> loader) {
		loaders.add(loader);
	}

	/**
	 * Returns the resource at the specified URI by loading it from the
	 * URI scheme's loader.
	 * @param uri to look up
	 * @return resource
	 */
	public Object getResource(URI uri) {
		// get the loader of the specified scheme
		String scheme = uri.getScheme();
		ResourceLoader<?> loader = getLoader(scheme);
		if (loader == null) {
			throw new LoaderNotFoundException("could not find loader for scheme " + scheme);
		}

		// load from the path
		InputStream in = new BufferedInputStream(ResourceManager.class.getResourceAsStream("/" + uri.getHost() + uri.getPath()));
		Object r = loader.load(in);

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * Returns the resource at the specified URI by loading it from the
	 * URI scheme's loader.
	 * @param uri to look up
	 * @return resource
	 */
	public Object getResource(String uri) {
		try {
			return getResource(new URI(uri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
