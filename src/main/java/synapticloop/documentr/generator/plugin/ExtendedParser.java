package synapticloop.documentr.generator.plugin;

import org.parboiled.Rule;
import org.parboiled.support.Var;
import org.pegdown.Extensions;
import org.pegdown.Parser;

/*
 * Copyright (c) 2016 Synapticloop.
 * 
 * All rights reserved.
 * 
 * This code may contain contributions from other parties which, where 
 * applicable, will be listed in the default build file for the project 
 * ~and/or~ in a file named CONTRIBUTORS.txt in the root of the project.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENSE.txt shipped with 
 * this source code or binaries.
 */

public class ExtendedParser extends Parser {
	public ExtendedParser() {
		super(Extensions.NONE, new Long(2000), Parser.DefaultParseRunnerProvider);
	}

	@Override
	public Rule CodeFence(Var<Integer> markerLength) {
		System.out.println("super-found a code fence");
		return(super.CodeFence(markerLength));
	}

}
