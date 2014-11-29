package org.omg.bpmn.miwg.mvn.test;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.omg.bpmn.miwg.api.AnalysisJob;
import org.omg.bpmn.miwg.api.AnalysisResult;
import org.omg.bpmn.miwg.test.common.InstanceParameter;
import org.omg.bpmn.miwg.test.common.ScanUtil;
import org.omg.bpmn.miwg.test.parameters.ReferenceScanParameters;
import org.omg.bpmn.miwg.util.DOMFactory;
import org.omg.bpmn.miwg.xmlCompare.XmlCompareAnalysisTool;
import org.omg.bpmn.miwg.xpath.XPathAnalysisTool;
import org.omg.bpmn.miwg.xsd.XsdAnalysisTool;
import org.w3c.dom.Document;

@RunWith(Parameterized.class)
public class Reference_All_Test {

	@Parameters
	public static List<Object[]> data() throws IOException {
		return ScanUtil.data(new ReferenceScanParameters());
	}

	protected InstanceParameter param;

	public Reference_All_Test(InstanceParameter parameter) {
		this.param = parameter;
	}

	@Before
	public void setUp() throws Exception {
		System.out.println();
		System.out.println("=============================");
		System.out.println("Running test:");
		System.out.println(param);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testXpath() throws Exception {
		XPathAnalysisTool tool = new XPathAnalysisTool();

		AnalysisJob job = new AnalysisJob(param.application.toString(),
				param.testResult.name, null, null, null);

		Document bpmnXmlDOM = DOMFactory.getDocument(param.testResult.file);
		AnalysisResult result = tool.analyzeDOM(job, null, bpmnXmlDOM,
				param.outputRoot);

		assertEquals(0, result.numFindings);

		System.out.println();
		System.out.println(result);
	}

	@Test
	public void testSchema() throws Exception {
		XsdAnalysisTool tool = new XsdAnalysisTool();

		AnalysisJob job = new AnalysisJob(param.application.toString(),
				param.testResult.name, null, null, null);

		InputStream bpmnXmlStream = new FileInputStream(param.testResult.file);
		try {
			AnalysisResult result = tool.analyzeStream(job, null,
					bpmnXmlStream, param.outputRoot);

			assertEquals(0, result.numFindings);

			System.out.println();
			System.out.println(result);
		} finally {
			bpmnXmlStream.close();
		}
	}

	/***
	 * This test checks that there is no difference between the same DOMs.
	 * 
	 */
	@Test
	public void testXmlCompare() throws Exception {
		XmlCompareAnalysisTool tool = new XmlCompareAnalysisTool();

		AnalysisJob job = new AnalysisJob(param.application.toString(),
				param.testResult.name, null, null, null);

		Document bpmnXmlDOM1 = DOMFactory.getDocument(param.testResult.file);
		Document bpmnXmlDOM2 = DOMFactory.getDocument(param.testResult.file);

		AnalysisResult result = tool.analyzeDOM(job, bpmnXmlDOM1, bpmnXmlDOM2,
				param.outputRoot);

		assertEquals(0, result.numFindings);

		System.out.println();
		System.out.println(result);
	}

}