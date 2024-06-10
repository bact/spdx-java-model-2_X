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

import java.util.Arrays;
import java.util.List;

import org.spdx.core.DefaultModelStore;
import org.spdx.core.IModelCopyManager;
import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.ModelRegistry;
import org.spdx.library.model.compat.v2.MockCopyManager;
import org.spdx.library.model.compat.v2.MockModelStore;
import org.spdx.library.model.v2.SpdxConstantsCompatV2;
import org.spdx.library.model.v2.SpdxModelFactory;
import org.spdx.library.model.v2.SpdxModelInfoV2_X;
import org.spdx.library.model.v2.license.ConjunctiveLicenseSet;
import org.spdx.library.model.v2.license.ExtractedLicenseInfo;
import org.spdx.storage.IModelStore;
import org.spdx.storage.IModelStore.IdType;

import junit.framework.TestCase;

/**
 * @author gary
 *
 */
public class ConjunctiveLicenseSetTest extends TestCase {
	
	static final String DOCUMENT_URI = "https://test.document.uri";
	String[] IDS = new String[] {"LicenseRef-id1", "LicenseRef-id2", "LicenseRef-id3", "LicenseRef-id4"};
	String[] TEXTS = new String[] {"text1", "text2", "text3", "text4"};
	ExtractedLicenseInfo[] NON_STD_LICENSES;
	
	IModelStore modelStore;
	IModelCopyManager copyManager;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		modelStore = new MockModelStore();
		copyManager = new MockCopyManager();
		ModelRegistry.getModelRegistry().registerModel(new SpdxModelInfoV2_X());
		DefaultModelStore.initialize(modelStore, "http://defaultdocument", copyManager);
		NON_STD_LICENSES = new ExtractedLicenseInfo[IDS.length];
		for (int i = 0; i < IDS.length; i++) {
			NON_STD_LICENSES[i] = new ExtractedLicenseInfo(modelStore, DOCUMENT_URI, IDS[i], copyManager, true);
			NON_STD_LICENSES[i].setExtractedText(TEXTS[i]);
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateConjunctive() throws InvalidSPDXAnalysisException {
		String id = modelStore.getNextId(IdType.Anonymous);
		ConjunctiveLicenseSet cls = new ConjunctiveLicenseSet(modelStore, DOCUMENT_URI, id, copyManager, true);
		cls.setMembers(Arrays.asList(NON_STD_LICENSES));
		ConjunctiveLicenseSet cls2 = (ConjunctiveLicenseSet) SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, id, SpdxConstantsCompatV2.CLASS_SPDX_CONJUNCTIVE_LICENSE_SET, copyManager);
		assertTrue(cls.equals(cls2));
		List<String> verify = cls2.verify();
		assertEquals(0, verify.size());
		verify = cls.verify();
		assertEquals(0, verify.size());
	}
	
	public void testAddMember() throws InvalidSPDXAnalysisException {
		String id = modelStore.getNextId(IdType.Anonymous);
		ConjunctiveLicenseSet cls = new ConjunctiveLicenseSet(modelStore, DOCUMENT_URI, id, copyManager, true);
		cls.setMembers(Arrays.asList(NON_STD_LICENSES));
		ExtractedLicenseInfo eli = new ExtractedLicenseInfo(modelStore, DOCUMENT_URI, "LicenseRef-test", copyManager, true);
		eli.setExtractedText("test text");
		List<String> verify = eli.verify();
		assertEquals(0, verify.size());
		verify = cls.verify();
		assertEquals(0, verify.size());
		cls.addMember(eli);
		verify = cls.verify();
		assertEquals(0, verify.size());
		assertEquals(NON_STD_LICENSES.length+1, cls.getMembers().size());
		assertTrue(cls.getMembers().contains(eli));
		ConjunctiveLicenseSet cls2 = (ConjunctiveLicenseSet) SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, id, SpdxConstantsCompatV2.CLASS_SPDX_CONJUNCTIVE_LICENSE_SET, copyManager);
		assertTrue(cls.equals(cls2));
		assertEquals(NON_STD_LICENSES.length+1, cls2.getMembers().size());
		assertTrue(cls2.getMembers().contains(eli));
		verify = cls.verify();
		assertEquals(0, verify.size());
		verify = cls2.verify();
		assertEquals(0, verify.size());
	}
	
	public void testRemoveMember() throws InvalidSPDXAnalysisException {
		String id = modelStore.getNextId(IdType.Anonymous);
		ConjunctiveLicenseSet cls = new ConjunctiveLicenseSet(modelStore, DOCUMENT_URI, id, copyManager, true);
		cls.setMembers(Arrays.asList(NON_STD_LICENSES));
		cls.removeMember(NON_STD_LICENSES[0]);
		assertEquals(NON_STD_LICENSES.length-1, cls.getMembers().size());
		assertFalse(cls.getMembers().contains(NON_STD_LICENSES[0]));
		ConjunctiveLicenseSet cls2 = (ConjunctiveLicenseSet) SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, id, SpdxConstantsCompatV2.CLASS_SPDX_CONJUNCTIVE_LICENSE_SET, copyManager);
		assertTrue(cls.equals(cls2));
		assertEquals(NON_STD_LICENSES.length-1, cls.getMembers().size());
		assertFalse(cls.getMembers().contains(NON_STD_LICENSES[0]));
		List<String> verify = cls2.verify();
		assertEquals(0, verify.size());
		verify = cls.verify();
		assertEquals(0, verify.size());
	}
}
