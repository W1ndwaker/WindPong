/*
 * (C) Copyright 2013 Walker Crouse.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 3 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.txt
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package net.windwaker.pong.resource;

import java.io.InputStream;

/**
 * Used for loading resources.
 * @param <R> resource type
 */
public abstract class ResourceLoader<R> {
	private final String scheme;

	public ResourceLoader(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * Loads the resource from the specified input stream.
	 * @param in to load from
	 * @return resource
	 */
	public abstract R load(InputStream in);

	/**
	 * Returns the scheme of the resource to preface URIs.
	 * @return scheme
	 */
	public String getScheme() {
		return scheme;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ResourceLoader
				&& ((ResourceLoader) obj).getScheme().equals(getScheme());
	}

	@Override
	public String toString() {
		return getScheme();
	}

	@Override
	public int hashCode() {
		return getScheme().hashCode();
	}
}
