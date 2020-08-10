package org.moskito.control.plugins.pagespeed;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 05.08.20 17:40
 */
public class Constants {
	public static String ELEMENTS[] = {
			"lighthouseResult.fetchTime",
			"lighthouseResult.categories.performance.score",
			"loadingExperience.metrics.FIRST_INPUT_DELAY_MS.category",
			"loadingExperience.metrics.FIRST_INPUT_DELAY_MS.percentile",
			"loadingExperience.metrics.CUMULATIVE_LAYOUT_SHIFT_SCORE.category",
			"loadingExperience.metrics.CUMULATIVE_LAYOUT_SHIFT_SCORE.percentile",
			"loadingExperience.metrics.LARGEST_CONTENTFUL_PAINT_MS.category",
			"loadingExperience.metrics.LARGEST_CONTENTFUL_PAINT_MS.percentile",
			"loadingExperience.metrics.FIRST_CONTENTFUL_PAINT_MS.category",
			"loadingExperience.metrics.FIRST_CONTENTFUL_PAINT_MS.percentile",
			"loadingExperience.overall_category",
			"lighthouseResult.audits.largest-contentful-paint.numericValue",
			"lighthouseResult.audits.largest-contentful-paint.score",
			"lighthouseResult.audits.max-potential-fid.numericValue",
			"lighthouseResult.audits.first-cpu-idle.numericValue",
			"lighthouseResult.audits.network-rtt.numericValue",
			"lighthouseResult.audits.interactive.numericValue",
			"lighthouseResult.audits.total-byte-weight.numericValue",
			"lighthouseResult.audits.server-response-time.numericValue",
	};

	public static String METRICS[] = {
			"firstCPUIdle",
			"observedFirstPaint",
			"interactive",
			"firstContentfulPaint",
			"firstMeaningfulPaint",
			"largestContentfulPaint",
			"speedIndex",
			"maxPotentialFID",

	};


}
