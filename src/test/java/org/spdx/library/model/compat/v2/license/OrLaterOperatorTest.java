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
package org.spdx.library.model.compat.v2.license;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.ExtractedLicenseInfo;
import org.spdx.library.model.v2.license.OrLaterOperator;
import org.spdx.library.model.v2.license.SimpleLicensingInfo;

import junit.framework.TestCase;

/**
 * @author Gary O'Neall
 *
 */
public class OrLaterOperatorTest extends TestCase {
	static final String LICENSE_ID1 = "LicenseRef-1";
	static final String LICENSE_TEXT1 = "licenseText";
	static final String LICENSE_ID2 = "LicenseRef-2";
	static final String LICENSE_TEXT2 = "Second licenseText";
	private SimpleLicensingInfo license1;
	private SimpleLicensingInfo license2;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		ModelRegistry.getModelRegistry().registerModel(new SpdxModelInfoV2_X());
		DefaultModelStore.initialize(new MockModelStore(), "http://defaultdocument", new MockCopyManager());
		license1 = new ExtractedLicenseInfo(LICENSE_ID1, LICENSE_TEXT1);
		license2 = new ExtractedLicenseInfo(LICENSE_ID2, LICENSE_TEXT2);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * Test method for {@link org.spdx.rdfparser.license.OrLaterOperator#hashCode()}.
	 * @throws org.spdx.library.InvalidSPDXAnalysisException 
	 */

	public void testHashCode() throws InvalidSPDXAnalysisException {
		SimpleLicensingInfo sameLicId = new ExtractedLicenseInfo(LICENSE_ID1, "different text");
		OrLaterOperator olo1 = new OrLaterOperator(license1);
		OrLaterOperator olo2 = new OrLaterOperator(license2);
		OrLaterOperator olo3 = new OrLaterOperator(sameLicId);
		assertFalse(olo1.hashCode() == olo2.hashCode());
		assertTrue(olo1.hashCode() == olo3.hashCode());
	}

	/**
	 * Test method for {@link org.spdx.rdfparser.license.OrLaterOperator#equals(java.lang.Object)}.
	 */

	public void testEqualsObject() throws InvalidSPDXAnalysisException {
		SimpleLicensingInfo sameLicId = new ExtractedLicenseInfo(LICENSE_ID1, "different text");
		OrLaterOperator olo1 = new OrLaterOperator(license1);
		OrLaterOperator olo2 = new OrLaterOperator(license2);
		OrLaterOperator olo3 = new OrLaterOperator(sameLicId);
		assertFalse(olo1.equals(olo2));
		assertTrue(olo1.equals(olo3));
	}

	public void testVerify() throws InvalidSPDXAnalysisException {
		OrLaterOperator olo1 = new OrLaterOperator(license1);
		assertEquals(0, olo1.verify().size());
		olo1.setLicense(null);
		assertEquals(1, olo1.verify().size());
	}

	/**
	 * Test method for {@link org.spdx.rdfparser.license.OrLaterOperator#setLicense(org.spdx.rdfparser.license.SimpleLicensingInfo)}.
	 * @throws InvalidSPDXAnalysisException 
	 */

	public void testSetLicense() throws InvalidSPDXAnalysisException {
		OrLaterOperator olo1 = new OrLaterOperator(license1);
		ExtractedLicenseInfo lic1 = (ExtractedLicenseInfo)olo1.getLicense();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());
		olo1.setLicense(license2);
		lic1 = (ExtractedLicenseInfo)olo1.getLicense();
		assertEquals(LICENSE_ID2, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT2, lic1.getExtractedText());
	}

	public void testDuplicatedOrLaterId() throws InvalidSPDXAnalysisException {
		OrLaterOperator olo1 = new OrLaterOperator(license1);
		OrLaterOperator comp = new OrLaterOperator(olo1.getModelStore(), olo1.getDocumentUri(), olo1.getId(), olo1.getCopyManager(), false);
		ExtractedLicenseInfo lic1 = (ExtractedLicenseInfo)olo1.getLicense();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());

		ExtractedLicenseInfo compLic = (ExtractedLicenseInfo)comp.getLicense();
		assertEquals(LICENSE_ID1, compLic.getLicenseId());
		assertEquals(LICENSE_TEXT1, compLic.getExtractedText());
	}
	

	public void testEquivalent() throws InvalidSPDXAnalysisException {
		OrLaterOperator olo1 = new OrLaterOperator(license1);
		assertTrue(olo1.equivalent(olo1));
		OrLaterOperator olo2 = new OrLaterOperator(license1);
		assertTrue(olo1.equivalent(olo2));
		OrLaterOperator olo3 = new OrLaterOperator(license2);
		assertFalse(olo1.equivalent(olo3));
	}
	
}
