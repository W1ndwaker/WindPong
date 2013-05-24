package net.windwaker.pong.resource;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Resource management.
 */
public class ResourceManager {
	private final Set<ResourceLoader<?>> loaders = new HashSet<>();

	/**
	 * Returns all registered loaders.
	 *
	 * @return registered loaders.
	 */
	public Set<ResourceLoader<?>> getLoaders() {
		return Collections.unmodifiableSet(loaders);
	}

	/**
	 * Returns the loader with the specified scheme.
	 *
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
	 *
	 * @param loader to register
	 */
	public void registerLoader(ResourceLoader<?> loader) {
		loaders.add(loader);
	}

	/**
	 * Returns the resource at the specified URI by loading it from the
	 * URI scheme's loader.
	 *
	 * @param uri to look up
	 * @return resource
	 */
	public Object getResource(URI uri) {
		// get the input stream of the resource
		InputStream in = ResourceManager.super.getClass().getResourceAsStream("/" + uri.getHost() + uri.getPath());
		if (in == null) {
			throw new IllegalArgumentException("could not find input stream");
		}

		// get the loader of the specified scheme
		String scheme = uri.getScheme();
		ResourceLoader<?> loader = getLoader(scheme);
		if (loader == null) {
			throw new IllegalArgumentException("could not find loader for scheme " + scheme);
		}

		return loader.load(in);
	}

	/**
	 * Returns the resource at the specified URI by loading it from the
	 * URI scheme's loader.
	 *
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
