/**
 * The MIT License (MIT)
 * Copyright (c) 2013 OMG BPMN Model Interchange Working Group
 *
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */

package org.omg.bpmn.miwg.xpath;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.omg.bpmn.miwg.api.AnalysisResult;
import org.omg.bpmn.miwg.api2.AnalysisJob2;
import org.omg.bpmn.miwg.api2.AnalysisTool2;
import org.omg.bpmn.miwg.common.Check2;
import org.omg.bpmn.miwg.common.CheckOutput;
import org.omg.bpmn.miwg.xpath.checks.A_1_0_Check;
import org.omg.bpmn.miwg.xpath.checks.A_1_1_Check;
import org.omg.bpmn.miwg.xpath.checks.A_1_2_Check;
import org.omg.bpmn.miwg.xpath.checks.A_2_0_Check;
import org.omg.bpmn.miwg.xpath.checks.A_3_0_Check;
import org.omg.bpmn.miwg.xpath.checks.A_4_0_Check;
import org.omg.bpmn.miwg.xpath.checks.A_4_1_Check;
import org.omg.bpmn.miwg.xpath.checks.B_1_0_Check;
import org.omg.bpmn.miwg.xpath.checks.B_2_0_Check;
import org.omg.bpmn.miwg.xpath.checks.C_1_0_Check;
import org.omg.bpmn.miwg.xpath.checks.DemoTechnicalSupportCheck;
import org.omg.bpmn.miwg.xpath.common.AbstractXpathCheck;
import org.w3c.dom.Document;

public class XPathAnalysisTool2 implements AnalysisTool2 {

	private List<AbstractXpathCheck> registeredChecks = new LinkedList<AbstractXpathCheck>();

	public XPathAnalysisTool2() {
		registerCheck(new A_1_0_Check());
		registerCheck(new A_1_1_Check());
		registerCheck(new A_1_2_Check());
		registerCheck(new A_2_0_Check());
		registerCheck(new A_3_0_Check());
		registerCheck(new A_4_0_Check());
		registerCheck(new A_4_1_Check());
		registerCheck(new B_1_0_Check());
		registerCheck(new B_2_0_Check());
		registerCheck(new C_1_0_Check());
		registerCheck(new DemoTechnicalSupportCheck());
	}

	public String getName() {
		return "xpath";
	}

	private void registerCheck(AbstractXpathCheck check) {
		registeredChecks.add(check);
	}

	private Check2 getCheck(AnalysisJob2 job) {
		for (AbstractXpathCheck check : registeredChecks) {
			if (check.isApplicable(job.MIWGTestCase)) {
				return check;
			}
		}
		throw new RuntimeException(String.format(
				"No applicable test found for %s", job.getName()));
	}

	@Override
	public AnalysisResult runAnalysis2(AnalysisJob2 job,
			Document referenceDocument, Document actualDocument, File logDir)
			throws Exception {
		Check2 check = getCheck(job);
		CheckOutput checkOutput = new CheckOutput(job.getName(), logDir);
		check.init(checkOutput);

		AnalysisResult result;

		try {
			result = check.execute2(actualDocument);
		} catch (Throwable e) {
			throw new IOException(e.getMessage(), e);
		} finally {
			checkOutput.close();
		}

		return result;
	}

}