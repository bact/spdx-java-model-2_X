package org.spdx.library.model.compat.v2.license;

import org.spdx.library.DefaultModelStore;
import org.spdx.library.InvalidSPDXAnalysisException;
import org.spdx.library.SpdxConstants.SpdxMajorVersion;
import org.spdx.library.model.compat.v2.GenericSpdxItem;
import org.spdx.storage.IModelStore;
import org.spdx.storage.simple.InMemSpdxStore;

import junit.framework.TestCase;

public class SpdxNoneLicenseTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		DefaultModelStore.reset(SpdxMajorVersion.VERSION_2);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		DefaultModelStore.reset(SpdxMajorVersion.VERSION_3);
	}

	public void testHashCodeEquals() throws InvalidSPDXAnalysisException {
		SpdxNoneLicense l1 = new SpdxNoneLicense();
		IModelStore store = new InMemSpdxStore(SpdxMajorVersion.VERSION_2);
		SpdxNoneLicense l2 = new SpdxNoneLicense(store, "https://doc.uri");
		assertEquals(l1.hashCode(), l2.hashCode());
		assertEquals(l1, l2);
		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l1));
	}
	
	public void testStoreRetrieveNoneLicense() throws InvalidSPDXAnalysisException {
		GenericSpdxItem item = new GenericSpdxItem();
		item.setLicenseConcluded(new SpdxNoneLicense());
		AnyLicenseInfo result = item.getLicenseConcluded();
		SpdxNoneLicense expected = new SpdxNoneLicense();
		assertEquals(expected, result);
	}
}
