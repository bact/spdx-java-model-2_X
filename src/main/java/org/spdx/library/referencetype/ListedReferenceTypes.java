/**
 * Copyright (c) 2019 Source Auditor Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.spdx.library.referencetype;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.library.model.v2.ReferenceType;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;

/**
 * Singleton class that maintains the current SPDX listed reference types.
 * <p>
 * The listed reference types are maintained by the SPDX community.
 *
 * @author Gary O'Neall
 */
public class ListedReferenceTypes {
	
	static final Logger logger = LoggerFactory.getLogger(ListedReferenceTypes.class);
	private static final ReadWriteLock listedReferenceTypesModificationLock = new ReentrantReadWriteLock();
	private static final String LISTED_REFERENCE_TYPE__RDF_LOCAL_DIR = "resources" + "/" + "listedexternaltypes";
	private static final String LISTED_REFERENCE_TYPE_PROPERTIES_FILENAME = LISTED_REFERENCE_TYPE__RDF_LOCAL_DIR + "/" + "listedreferencetypes.properties";
	private static final String LISTED_REFERENCE_TYPE_PROPERTIES_CLASS_PATH = "org/spdx/library/referencetype/listedreferencetypes.properties";
	private static final String PROPERTY_LISTED_REFERENCE_TYPES = "listedReferenceTypes";
	private static ListedReferenceTypes listedReferenceTypes;
	private Properties listedReferenceTypeProperties;
	List<String> listedReferenceNames = new ArrayList<String>();
	ConcurrentMap<String, ReferenceType> listedReferenceTypeCache = new ConcurrentHashMap<>();
	
	private ListedReferenceTypes() {
		listedReferenceTypeProperties  = new Properties();
        InputStream in = null;
        try {
        	try {
                in = ListedReferenceTypes.class.getResourceAsStream("/" + LISTED_REFERENCE_TYPE_PROPERTIES_FILENAME);
                if (Objects.nonNull(in)) {
                	listedReferenceTypeProperties.load(in);
                }
        	} catch (IOException e) {
                logger.warn("IO Exception reading listed reference type properties file: " 
                				+ e.getMessage() + ", loading properties from class properties file.", e);
        	}
        	if (Objects.isNull(in)) {
        		try {
                	in = ListedReferenceTypes.class.getClassLoader().getResourceAsStream(LISTED_REFERENCE_TYPE_PROPERTIES_CLASS_PATH);
                	if (Objects.nonNull(in)) {
                		listedReferenceTypeProperties.load(in);
                	} else {
                		logger.error("Unable to load listed reference type properties");
                	}
                } catch (IOException ex2) {
                	logger.error("IO exception reading listed reference type properties from class properties file: "+ex2.getMessage(), ex2);
                }
        	}
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn("Unable to close listed license properties file: " + e.getMessage(), e);
                }
            }
        }
        loadReferenceTypeNames();
	}
	
	/**
	 * 
	 */
	private void loadReferenceTypeNames() {
		listedReferenceTypesModificationLock.readLock().lock();
		try {
			String referenceTypeNamesStr = this.listedReferenceTypeProperties.getProperty(PROPERTY_LISTED_REFERENCE_TYPES);
			String[] referenceTypeNamesAr = referenceTypeNamesStr.split(",", -1);
			for (String name:referenceTypeNamesAr) {
				this.listedReferenceNames.add(name.trim());
			}
		} finally {
			listedReferenceTypesModificationLock.readLock().unlock();
		}
	}

	/**
	 * @return the listed reference types as maintained by the SPDX workgroup
	 */
	public static ListedReferenceTypes getListedReferenceTypes() {
    	listedReferenceTypesModificationLock.writeLock().lock();
        try {
            if (listedReferenceTypes == null) {
            	listedReferenceTypes = new ListedReferenceTypes();
            }
            return listedReferenceTypes;
        } finally {
        	listedReferenceTypesModificationLock.writeLock().unlock();
        }
	}
	
	/**
	 * Resets all of the listed reference types and reloads the listed reference ID's
	 * NOTE: This method should be used with caution, it will negatively impact
	 * performance.
	 * @return
	 */
    public static ListedReferenceTypes resetListedReferenceTypes() {
    	listedReferenceTypesModificationLock.writeLock().lock();
        try {
        	listedReferenceTypes = new ListedReferenceTypes();
            return listedReferenceTypes;
        } finally {
        	listedReferenceTypesModificationLock.writeLock().unlock();
        }
    }
    
    /**
     * Returns true if the URI references a valid SPDX listed reference type
     * @param uri
     * @return
     */
    public boolean isListedReferenceType(URI uri) {
    	if (uri.toString().startsWith(SpdxConstantsCompatV2.SPDX_LISTED_REFERENCE_TYPES_PREFIX)) {
    		String referenceTypeName = uri.toString().substring(SpdxConstantsCompatV2.SPDX_LISTED_REFERENCE_TYPES_PREFIX.length());
    		return this.listedReferenceNames.contains(referenceTypeName);
    	} else {
    		return false;
    	}
    }
    
    /**
     * Get the listed reference URI from a listed reference type name used in the tag/value format
     * @param listedReferenceName
     * @return
     * @throws InvalidSPDXAnalysisException
     */
    public URI getListedReferenceUri(String listedReferenceName) throws InvalidSPDXAnalysisException {
    	URI retval;
		try {
			retval = new URI(SpdxConstantsCompatV2.SPDX_LISTED_REFERENCE_TYPES_PREFIX + listedReferenceName);
		} catch (URISyntaxException e) {
			logger.error("Error forming listed license URI",e);
			throw new InvalidSPDXAnalysisException(listedReferenceName + " is not a valid SPDX listed reference type syntax.",e);
		}
    	if (!isListedReferenceType(retval)) {
    		throw new InvalidSPDXAnalysisException(listedReferenceName + " is not a valid SPDX listed reference type.");
    	}
    	return retval;
    }
    
    public ReferenceType getListedReferenceTypeByName(String listedReferenceName) throws InvalidSPDXAnalysisException {
    	ReferenceType retval = this.listedReferenceTypeCache.get(listedReferenceName);
    	if (retval == null) {
    		URI listedRefUri = getListedReferenceUri(listedReferenceName);
			retval = new ReferenceType(listedRefUri.toString());
			ReferenceType oldValue = this.listedReferenceTypeCache.putIfAbsent(listedReferenceName, retval);
			if (oldValue != null) {
				retval = oldValue;
			}
    	}
    	return retval;
    }

    /**
     * Get the listed reference type name from the listed reference URI
     * @param uri
     * @return SPDX listed reference type name used in the tag/value format
     * @throws InvalidSPDXAnalysisException
     */
    public String getListedReferenceName(URI uri) throws InvalidSPDXAnalysisException {
    	if (!this.isListedReferenceType(uri)) {
    		throw new InvalidSPDXAnalysisException(uri.toString() + " is not a valid URI for an SPDX listed reference type.");
    	}
    	return uri.toString().substring(SpdxConstantsCompatV2.SPDX_LISTED_REFERENCE_TYPES_PREFIX.length());
    }
    
    //TODO: Implement accessing the SPDX reference type pages directly similar to the SpdxListedLicenses class
}
