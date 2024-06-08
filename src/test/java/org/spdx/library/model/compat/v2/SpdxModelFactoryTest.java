package org.spdx.library.model.compat.v2;


import java.util.Optional;
import java.util.stream.Stream;

import org.spdx.library.ModelCopyManager;
import org.spdx.library.SpdxConstants.SpdxMajorVersion;
import org.spdx.library.model.compat.v2.Annotation;
import org.spdx.library.model.compat.v2.Checksum;
import org.spdx.library.model.compat.v2.ModelObject;
import org.spdx.library.model.compat.v2.SpdxDocument;
import org.spdx.library.model.compat.v2.SpdxElement;
import org.spdx.library.model.compat.v2.SpdxFile;
import org.spdx.storage.IModelStore;
import org.spdx.storage.simple.InMemSpdxStore;

import junit.framework.TestCase;

public class SpdxModelFactoryTest extends TestCase {
	
	static final String DOCUMENT_URI = "http://www.spdx.org/documents";
	static final String ID1 = SpdxConstantsCompatV2.SPDX_ELEMENT_REF_PRENUM + "1";
	static final String ID2 = SpdxConstantsCompatV2.SPDX_ELEMENT_REF_PRENUM + "2";
	
	IModelStore modelStore;
	ModelCopyManager copyManager;
	

	protected void setUp() throws Exception {
		super.setUp();
		modelStore = new InMemSpdxStore(SpdxMajorVersion.VERSION_2);
		copyManager = new ModelCopyManager();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateSpdxDocument() throws InvalidSPDXAnalysisException {
		fail("Unimplemented");
	}
	
	public void testCreateModelObject2() throws InvalidSPDXAnalysisException {
		fail("Unimplemented");
	}
	
	public void testGetModelObjectIModelStoreStringStringStringModelCopyManagerBoolean2() throws InvalidSPDXAnalysisException {
		fail("Unimplemented");
	}
	
	public void testTypeToClass() throws InvalidSPDXAnalysisException {
		fail("Unimplemented");
	}
	
	public void testGetElements() throws InvalidSPDXAnalysisException {
		fail("Unimplemented");
	}
	
	public void testClassUriToClass() throws InvalidSPDXAnalysisException {
		fail("Unimplemented");
	}
	
	public void testGetModelObjectIModelStoreStringStringModelCopyManager2() throws InvalidSPDXAnalysisException {
		fail("Unimplemented");
	}
	
	public void testCreateSpdxDocumentV2() throws InvalidSPDXAnalysisException {
		SpdxDocument result = SpdxModelFactory.createSpdxDocumentV2(modelStore, DOCUMENT_URI, copyManager);
		assertEquals(SpdxConstantsCompatV2.SPDX_DOCUMENT_ID, result.getId());
	}

	public void testCreateModelObjectV2() throws InvalidSPDXAnalysisException {
		ModelObjectV2 result = SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, ID1, 
				SpdxConstantsCompatV2.CLASS_SPDX_CHECKSUM, copyManager);
		assertTrue(result instanceof Checksum);
		assertEquals(ID1, result.getId());
	}

	public void testGetModelObjectIModelStoreStringStringStringModelCopyManagerBooleanV2() throws InvalidSPDXAnalysisException {
		ModelObjectV2 result = SpdxModelFactory.getModelObjectV2(modelStore, DOCUMENT_URI, ID1, 
				SpdxConstantsCompatV2.CLASS_SPDX_CHECKSUM, copyManager, true);
		assertTrue(result instanceof Checksum);
		assertEquals(ID1, result.getId());
		ModelObjectV2 result2 = SpdxModelFactory.getModelObjectV2(modelStore, DOCUMENT_URI, ID1, 
				SpdxConstantsCompatV2.CLASS_SPDX_CHECKSUM, copyManager, false);
		assertTrue(result2 instanceof Checksum);
		assertEquals(ID1, result2.getId());
		try {
			result = SpdxModelFactory.getModelObjectV2(modelStore, DOCUMENT_URI, ID2, 
					SpdxConstantsCompatV2.CLASS_SPDX_CHECKSUM, copyManager, false);
			fail("Expected objectUri not found exception");
		} catch(SpdxIdNotFoundException ex) {
			// expected
		}
	}

	public void testTypeToClassV2() throws InvalidSPDXAnalysisException {
		assertEquals(Checksum.class, SpdxModelFactory.typeToClass(SpdxConstantsCompatV2.CLASS_SPDX_CHECKSUM,
				SpdxMajorVersion.VERSION_2));
		assertEquals(SpdxFile.class, SpdxModelFactory.typeToClass(SpdxConstantsCompatV2.CLASS_SPDX_FILE,
				SpdxMajorVersion.VERSION_2));
	}

	@SuppressWarnings("unchecked")
	public void testGetElementsV2() throws InvalidSPDXAnalysisException {
		ModelObjectV2 file1 = SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, ID1, 
				SpdxConstantsCompatV2.CLASS_SPDX_FILE, copyManager);
		ModelObjectV2 file2 = SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, ID2, 
				SpdxConstantsCompatV2.CLASS_SPDX_FILE, copyManager);
		try (Stream<SpdxElement> elementStream = (Stream<SpdxElement>)SpdxModelFactory.getElements(modelStore, DOCUMENT_URI, copyManager, SpdxFile.class)) {
		    elementStream.forEach(element -> {
		        assertTrue(element instanceof SpdxFile);
	            SpdxFile result = (SpdxFile)element;
	            if (result.getId().equals(ID1)) {
	                try {
	                    assertTrue(file1.equivalent(result));
	                } catch (InvalidSPDXAnalysisException e) {
	                    fail("Error: "+e.getMessage());
	                }
	            } else {
	                try {
	                    assertTrue(file2.equivalent(result));
	                } catch (InvalidSPDXAnalysisException e) {
	                    fail("Error: "+e.getMessage());
	                }
	            }
		    });
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testGetElementsNamespace() throws InvalidSPDXAnalysisException {
		ModelObjectV2 file1 = SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, ID1, 
				SpdxConstantsCompatV2.CLASS_SPDX_FILE, copyManager);
		ModelObjectV2 file2 = SpdxModelFactory.createModelObjectV2(modelStore, DOCUMENT_URI, ID2, 
				SpdxConstantsCompatV2.CLASS_SPDX_FILE, copyManager);
		try (Stream<SpdxElement> elementStream = (Stream<SpdxElement>)SpdxModelFactory.getElements(modelStore, DOCUMENT_URI + "#", copyManager, SpdxFile.class)) {
		    elementStream.forEach(element -> {
		        assertTrue(element instanceof SpdxFile);
	            SpdxFile result = (SpdxFile)element;
	            if (result.getId().equals(ID1)) {
	                try {
	                    assertTrue(file1.equivalent(result));
	                } catch (InvalidSPDXAnalysisException e) {
	                    fail("Error: "+e.getMessage());
	                }
	            } else {
	                try {
	                    assertTrue(file2.equivalent(result));
	                } catch (InvalidSPDXAnalysisException e) {
	                    fail("Error: "+e.getMessage());
	                }
	            }
		    });
		}
	}

	public void testClassUriToClassV2() throws InvalidSPDXAnalysisException {
		assertEquals(Annotation.class, 
				SpdxModelFactory.classUriToClass(SpdxConstantsCompatV2.SPDX_NAMESPACE + SpdxConstantsCompatV2.CLASS_ANNOTATION,
						SpdxMajorVersion.VERSION_2));
	}

	public void testGetModelObjectIModelStoreStringStringModelCopyManagerV2() throws InvalidSPDXAnalysisException {
		ModelObjectV2 result = SpdxModelFactory.getModelObjectV2(modelStore, DOCUMENT_URI, ID1, 
				SpdxConstantsCompatV2.CLASS_SPDX_CHECKSUM, copyManager, true);
		assertTrue(result instanceof Checksum);
		assertEquals(ID1, result.getId());
		Optional<ModelObjectV2> result2 = SpdxModelFactory.getModelObjectV2(modelStore, DOCUMENT_URI, ID1, copyManager);
		assertTrue(result2.isPresent());
		assertTrue(result2.get() instanceof Checksum);
		assertEquals(ID1, result2.get().getId());
		result2 = SpdxModelFactory.getModelObjectV2(modelStore, DOCUMENT_URI, ID2, copyManager);
		assertFalse(result2.isPresent());
	}

}
