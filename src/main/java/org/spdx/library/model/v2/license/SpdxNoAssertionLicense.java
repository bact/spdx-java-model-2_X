/**
 * Copyright (c) 2011 Source Auditor Inc.
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
 *
*/
package org.spdx.library.model.v2.license;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.IndividualUriValue;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.SimpleUriValue;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

/**
 * Special class of license to represent no asserted license license in the file or packages
 * @author Gary O'Neall
 *
 */
public class SpdxNoAssertionLicense extends AnyLicenseInfo implements IndividualUriValue {
	
	static final String NOASSERTION_LICENSE_ID = "NOASSERTION_LICENSE_ID";
	
	/**
	 * Create a new No Assertion license with the default store and default document URI
	 * @throws InvalidSPDXAnalysisException 
	 */
	public SpdxNoAssertionLicense() throws InvalidSPDXAnalysisException {
		super(DefaultModelStore.getDefaultModelStore().getNextId(IdType.Anonymous));
	}

	public SpdxNoAssertionLicense(IModelStore modelStore, String documentUri)
			throws InvalidSPDXAnalysisException {
		super(modelStore, documentUri, modelStore.getNextId(IdType.Anonymous), null, true);
	}
	
	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.license.AnyLicenseInfo#toString()
	 */
	@Override
	public String toString() {
		return SpdxConstantsCompatV2.NOASSERTION_VALUE;
	}
	
	@Override
	public boolean equals(Object comp) {
		return SimpleUriValue.isIndividualUriValueEquals(this, comp);
	}

	@Override
	public int hashCode() {
		return SimpleUriValue.getIndividualUriValueHash(this);
	}

	/* (non-Javadoc)
	 * @see org.spdx.rdfparser.license.AnyLicenseInfo#verify()
	 */
	@Override
	protected List<String> _verify(Set<String> verifiedIds, String specVersion) {
		return new ArrayList<>();
	}

	@Override
	public String getType() {
		return SpdxConstantsCompatV2.CLASS_NOASSERTION_LICENSE;
	}

	@Override
	public String getIndividualURI() {
		return SpdxConstantsCompatV2.URI_VALUE_NOASSERTION;
	}
}
