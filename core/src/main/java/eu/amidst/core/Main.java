/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package eu.amidst.core;


import eu.amidst.core.distribution.Multinomial;
import eu.amidst.core.distribution.Normal;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.core.utils.BayesianNetworkGenerator;
import eu.amidst.core.variables.Variable;

/**
 * Created by andresmasegosa on 24/6/15.
 */
public class Main {

    public static void main (String[] args){

        BayesianNetworkGenerator.setNumberOfGaussianVars(2);
        BayesianNetworkGenerator.setNumberOfMultinomialVars(2,2);
        BayesianNetworkGenerator.setNumberOfLinks(3);

        BayesianNetwork bn = BayesianNetworkGenerator.generateBayesianNetwork();

        Variable normalVar = bn.getVariables().getVariableByName("GaussianVar0");
        Normal normalDist = bn.getConditionalDistribution(normalVar);
        normalDist.setMean(1.0);
        normalDist.setVariance(1.0);

        Variable multiVar = bn.getVariables().getVariableByName("DiscreteVar0");
        Multinomial multinomial = bn.getConditionalDistribution(multiVar);
        multinomial.setProbabilities(new double[]{0.2, 0.8});
    }

}
