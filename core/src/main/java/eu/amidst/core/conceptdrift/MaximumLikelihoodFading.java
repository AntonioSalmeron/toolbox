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

package eu.amidst.core.conceptdrift;


import com.google.common.util.concurrent.AtomicDouble;
import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataOnMemory;
import eu.amidst.core.exponentialfamily.EF_BayesianNetwork;
import eu.amidst.core.exponentialfamily.SufficientStatistics;
import eu.amidst.core.learning.parametric.ParallelMaximumLikelihood;

/**
 * Created by andresmasegosa on 9/6/15.
 */
public class MaximumLikelihoodFading extends ParallelMaximumLikelihood implements FadingLearner {

    double fadingFactor;

    public double getFadingFactor() {
        return fadingFactor;
    }

    @Override
    public void setFadingFactor(double fadingFactor) {
        this.fadingFactor = fadingFactor;
    }

    @Override
    public double updateModel(DataOnMemory<DataInstance> batch) {

        SufficientStatistics batchSS = batch.stream()
                .map(efBayesianNetwork::getSufficientStatistics)
                .reduce(SufficientStatistics::sumVector).get();

        sumSS.multiplyBy(fadingFactor);
        sumSS.sum(batchSS);

        dataInstanceCount.set(dataInstanceCount.get() * fadingFactor + batch.getNumberOfDataInstances());

        return Double.NaN;
    }



    @Override
    public void runLearning() {

        efBayesianNetwork = new EF_BayesianNetwork(dag);

        dataInstanceCount = new AtomicDouble(0);
        sumSS = efBayesianNetwork.createZeroedSufficientStatistics();
        for (DataOnMemory<DataInstance> batch : dataStream.iterableOverBatches(batchSize)){
            SufficientStatistics batchSS = batch.stream()
                    .map(efBayesianNetwork::getSufficientStatistics)
                    .reduce(SufficientStatistics::sumVector).get();

            sumSS.multiplyBy(fadingFactor);
            sumSS.sum(batchSS);

            dataInstanceCount.set(dataInstanceCount.get()*fadingFactor + batchSize);
        }
    }


    @Override
    public void setParallelMode(boolean parallelMode) {
        throw new UnsupportedOperationException("Non Parallel Mode Supported.");
    }
}
