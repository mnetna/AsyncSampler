package org.example.sampler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Created by lees on 2020-05-12
 */
public class HttpAsyncSampler extends AbstractJavaSamplerClient implements Serializable {
    private static final Logger LOG = LoggingManager.getLoggerForClass();

    private static final String LABEL = "LABEL";
    private static final String DEFAULT_LABEL = "HTTPAsyncSampler";
    private static final String THREADS = "Thread Count";
    private static final String DEFAULT_THREADS = "1";
    private static final String URL = "URL";
    private static final String DEFAULT_URL = "";
    private static final String SAMPLER_DATA = "DATA";
    private static final String DEFAULT_SAMPLER_DATA = "";
    private static final int SLEEP_TIME = 5000;

    public HttpAsyncSampler() {
        LOG.info("▶▶▶ Simple Async Sampler Start!!!");
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument(LABEL, DEFAULT_LABEL);
        params.addArgument(URL, DEFAULT_URL);
        params.addArgument(THREADS, DEFAULT_THREADS);
        params.addArgument(SAMPLER_DATA, DEFAULT_SAMPLER_DATA);
        return params;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        String label = context.getParameter(LABEL);
        String path = context.getParameter(URL);
        String data = context.getParameter(SAMPLER_DATA);

//        int threads = context.getIntParameter(THREADS);
//        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(threads);
        SampleResult results = new SampleResult();
        results.setSampleLabel(label);

        try {
            results.sampleStart();
            results.setSuccessful(true);

            CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
            httpclient.start();
            final HttpPost request = new HttpPost(path);
            StringEntity requestEntity = new StringEntity(data, "utf-8");
            request.setEntity(requestEntity);

            Future<HttpResponse> future = httpclient.execute(request, null);
            HttpResponse response = future.get();

        } catch (InterruptedException e) {
            LOG.error("InterruptedException!!!", e);
            results.setSuccessful(false);
        } catch (ExecutionException e) {
            LOG.error("ExecutionException!!!", e);
            results.setSuccessful(false);
        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException!!!", e);
            results.setSuccessful(false);
        } catch (Exception e) {
            LOG.error("Exception!!!", e);
            results.setSuccessful(false);
        }

        results.sampleEnd();

        return results;
    }
}
