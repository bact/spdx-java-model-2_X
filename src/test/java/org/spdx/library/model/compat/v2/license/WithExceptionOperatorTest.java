package org.spdx.library.model.compat.v2.license;

import java.util.List;

import org.spdx.library.DefaultModelStore;
import org.spdx.library.InvalidSPDXAnalysisException;
import org.spdx.library.ModelCopyManager;
import org.spdx.library.SpdxConstantsCompatV2;
import org.spdx.library.SpdxModelFactory;
import org.spdx.library.TypedValue;
import org.spdx.library.SpdxConstants.SpdxMajorVersion;
import org.spdx.storage.IModelStore;
import org.spdx.storage.compat.v2.CompatibleModelStoreWrapper;
import org.spdx.storage.simple.InMemSpdxStore;

import junit.framework.TestCase;

public class WithExceptionOperatorTest extends TestCase {
	
	static final String LICENSE_ID1 = "LicenseRef-1";
	static final String LICENSE_TEXT1 = "licenseText";
	static final String EXCEPTION_ID1 = "Exception-1";
	static final String EXCEPTION_NAME1 = "ExceptionName";
	static final String EXCEPTION_TEXT1 = "ExceptionText";
	static final String LICENSE_ID2 = "LicenseRef-2";
	static final String LICENSE_TEXT2 = "Second licenseText";
	static final String EXCEPTION_ID2 = "Exception-2";
	static final String EXCEPTION_NAME2 = "Second ExceptionName";
	static final String EXCEPTION_TEXT2 = "Second ExceptionText";

	private SimpleLicensingInfo license1;
	private SimpleLicensingInfo license2;
	private LicenseException exception1;
	private LicenseException exception2;

	protected void setUp() throws Exception {
		super.setUp();
		DefaultModelStore.reset(SpdxMajorVersion.VERSION_2);
		license1 = new ExtractedLicenseInfo(LICENSE_ID1, LICENSE_TEXT1);
		license2 = new ExtractedLicenseInfo(LICENSE_ID2, LICENSE_TEXT2);
		exception1 = new LicenseException(EXCEPTION_ID1, EXCEPTION_NAME1,
				EXCEPTION_TEXT1);
		exception2 = new LicenseException(EXCEPTION_ID2, EXCEPTION_NAME2,
				EXCEPTION_TEXT2);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		DefaultModelStore.reset(SpdxMajorVersion.VERSION_3);
	}

	public void testHashCode() throws InvalidSPDXAnalysisException {
		SimpleLicensingInfo sameLicId = new ExtractedLicenseInfo(LICENSE_ID1, "different text");
		LicenseException sameExceptionId = new LicenseException(EXCEPTION_ID1, "different Name",
				"different exception text"); 
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		WithExceptionOperator weo2 = new WithExceptionOperator(license2, exception2);
		WithExceptionOperator weoSameIdAs1 = new WithExceptionOperator(sameLicId, sameExceptionId);
		assertFalse(weo1.hashCode() == weo2.hashCode());
		assertTrue(weo1.hashCode() == weoSameIdAs1.hashCode());
	}

	public void testEqualsObject() throws InvalidSPDXAnalysisException {
		SimpleLicensingInfo sameLicId = new ExtractedLicenseInfo(LICENSE_ID1, "different text");
		LicenseException sameExceptionId = new LicenseException(EXCEPTION_ID1, "different Name",
				"different exception text"); 
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		WithExceptionOperator weo2 = new WithExceptionOperator(license2, exception2);
		WithExceptionOperator weoSameIdAs1 = new WithExceptionOperator(sameLicId, sameExceptionId);
		assertFalse(weo1.equals(weo2));
		assertTrue(weo1.equals(weoSameIdAs1));
	}


	public void testVerify() throws InvalidSPDXAnalysisException {
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		assertEquals(0, weo1.verify().size());
		weo1.setException(null);
		assertEquals(1, weo1.verify().size());
		weo1.setLicense(null);
		assertEquals(2, weo1.verify().size());
	}


	public void testCopy() throws InvalidSPDXAnalysisException {
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		IModelStore store = new InMemSpdxStore(SpdxMajorVersion.VERSION_2);
		ModelCopyManager copyManager = new ModelCopyManager();
		@SuppressWarnings("unused")
		TypedValue tv = copyManager.copy(store, weo1.getModelStore(),
				CompatibleModelStoreWrapper.documentUriIdToUri(weo1.getDocumentUri(), weo1.getId(), weo1.getModelStore()),
				weo1.getType(), weo1.getDocumentUri(), DefaultModelStore.getDefaultDocumentUri(),
				weo1.getDocumentUri(), DefaultModelStore.getDefaultDocumentUri());
		WithExceptionOperator clone = (WithExceptionOperator) SpdxModelFactory.createModelObjectV2(store, 
				DefaultModelStore.getDefaultDocumentUri(), weo1.getId(), SpdxConstantsCompatV2.CLASS_WITH_EXCEPTION_OPERATOR, copyManager);
		ExtractedLicenseInfo lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		ExtractedLicenseInfo lic1FromClone = (ExtractedLicenseInfo)clone.getLicense();
		assertEquals(lic1.getExtractedText(), lic1FromClone.getExtractedText());
		LicenseException le1 = weo1.getException();
		LicenseException le1FromClone = clone.getException();
		assertEquals(le1.getLicenseExceptionId(), le1FromClone.getLicenseExceptionId());
		assertEquals(le1.getLicenseExceptionText(), le1FromClone.getLicenseExceptionText());
		assertEquals(le1.getName(), le1FromClone.getName());
	}


	public void testSetLicense() throws InvalidSPDXAnalysisException {
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		ExtractedLicenseInfo lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		LicenseException le1 = weo1.getException();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID1, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT1, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME1, le1.getName());
		weo1.setLicense(license2);
		lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		le1 = weo1.getException();
		assertEquals(LICENSE_ID2, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT2, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID1, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT1, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME1, le1.getName());
	}



	public void testSetException() throws InvalidSPDXAnalysisException {
		WithExceptionOperator weo1 = new WithExceptionOperator(license1, exception1);
		ExtractedLicenseInfo lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		LicenseException le1 = weo1.getException();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID1, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT1, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME1, le1.getName());
		weo1.setException(exception2);
		lic1 = (ExtractedLicenseInfo)weo1.getLicense();
		le1 = weo1.getException();
		assertEquals(LICENSE_ID1, lic1.getLicenseId());
		assertEquals(LICENSE_TEXT1, lic1.getExtractedText());
		assertEquals(EXCEPTION_ID2, le1.getLicenseExceptionId());
		assertEquals(EXCEPTION_TEXT2, le1.getLicenseExceptionText());
		assertEquals(EXCEPTION_NAME2, le1.getName());
	}
	
	public void testClassPathException() throws InvalidSPDXAnalysisException, InvalidLicenseStringException {
		List<String> result = LicenseInfoFactory.parseSPDXLicenseV2String("GPL-2.0-only WITH Classpath-exception-2.0").verify();
		assertTrue(result.isEmpty());
	}
}
