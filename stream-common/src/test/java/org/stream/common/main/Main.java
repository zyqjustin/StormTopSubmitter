package org.stream.common.main;

import org.stream.common.service.TopologyLocalTestSubmitter;

public class Main {

	public static void main(String[] args) throws Exception {
		TopologyLocalTestSubmitter submitter = new TopologyLocalTestSubmitter();
		submitter.run();
	}
}
