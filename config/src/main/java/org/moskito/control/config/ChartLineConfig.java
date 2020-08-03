package org.moskito.control.config;

import com.google.gson.annotations.SerializedName;
import net.anotheria.util.StringUtils;
import org.configureme.annotations.ConfigureMe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Configuration object for a chart line - a line in a chart.
 *
 * @author lrosenberg
 * @since 18.06.13 13:53
 */
@ConfigureMe
public class ChartLineConfig {
	/**
	 * Name of the component.
	 */
	@SerializedName("component")
	private String component;
	/**
	 * Name of the accumulator.
	 */
	@SerializedName("accumulator")
	private String accumulator;
	
	/**
	 * Caption for the chart line.
	 */
	@SerializedName("caption")
	private String caption;

	@SerializedName("componentTags")
	private String componentTags;

	public void setComponent(String component) {
		this.component = component;
	}

	public String getComponent() {
		return component;
	}

	public String getAccumulator() {
		return accumulator;
	}

	public void setAccumulator(String accumulator) {
		this.accumulator = accumulator;
	}

	public String getComponentTags() {
		return componentTags;
	}

	public void setComponentTags(String componentTags) {
		this.componentTags = componentTags;
	}

	/**
	 * Method used to get caption in case this config creates
	 * multiple charts.
	 * If caption present in config, returns caption string
	 * with appended component name in round bracers.
	 * Else return null
	 *
	 * @param componentName name of component to get caption
	 *
	 * @return caption string
	 */
	public String getCaption(String componentName) {
		return getCaption() != null ?  getCaption() + " (" + componentName + ")" : null;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public ChartLineComponentMatcher getComponentsMatcher() {
		return new ChartLineComponentMatcher();
	}

	/**
	 * Helper class to finding components chart line
	 * by pattern specified in chart line config
	 */
	public class ChartLineComponentMatcher {

		/**
		 * Name of component category
		 * Only components with this category will match
		 * If null - all categories is match
		 */
		private String categoryName;
		/**
		 * Pattern for component name
		 */
		private Pattern namePattern;

		private Set<String> componentTagsSet = Collections.emptySet();

		/**
		 * Parses component name pattern string
		 */
		private ChartLineComponentMatcher(){

			if (componentTags!=null && componentTags.length()>0){
				String[] tagsArray = StringUtils.tokenize(componentTags, ',');
				componentTagsSet = new HashSet<>();
				for (String t: tagsArray){
					componentTagsSet.add(t);
				}
			}

			String componentNamePatternString; // Part of pattern string belongs to component name

			if(component.contains(":")){ // Means component category specified in config

				// Split pattern string to category (0 index) and name (1 index)
				String[] splittedPatternStrings = component.split(":");
				categoryName = splittedPatternStrings[0].trim();
				componentNamePatternString = splittedPatternStrings[1].trim();

			}
			else
				// All pattern string belongs to component name. Any category will match
				componentNamePatternString = component;

			// Pattern to match name string
			String componentPattern = "";

			// Adding zero or more symbols to begin of regexp if there is asterisks in beginning
			if(componentNamePatternString.charAt(0) == '*') {
				componentPattern = ".*";
				componentNamePatternString = componentNamePatternString.substring(1); // removing asterisks
			}

			// Check length in case initial pattern string contains only one asterisks symbol
			if(componentNamePatternString.length() > 0)

				// Adding zero or more symbols to end of regexp if there is asterisks on end
				if(componentNamePatternString.charAt(componentNamePatternString.length() - 1) == '*'){

					// Check for cases there is only two asterisks in pattern string
					if(componentNamePatternString.length() > 0)
						// Quoting component name string without asterisks
						componentPattern += Pattern.quote(
								componentNamePatternString.substring(
										0,
										componentNamePatternString.length() - 1
								)
						);

					// Adding zero or more symbols to end of regexp
					componentPattern += ".*";

				}
				else
					// No asterisks on end of pattern. Quoting component name string
					componentPattern += Pattern.quote(componentNamePatternString);

			namePattern = Pattern.compile(componentPattern);

		}

		/**
		 * Check is component name and category matches to this pattern.
		 * @param categoryName component category name
		 * @param componentName component name
		 * @return true  - component matches
		 * 		   false - no
		 */
        private boolean isComponentMatches(String categoryName, String componentName, List<String> tags){
			       // Category name is not specified or matches to category in arguments
			return (this.categoryName == null || this.categoryName.equals(categoryName)) &&
					// And component name matches to pattern
					(namePattern.matcher(componentName).find());

		}


		/**
		 * Finds components names with name and category matches to this pattern
		 * from components config array
		 * @param components components to find matches
		 * @return array of matched components names
		 */
		/*public String[] getMatchedComponents(Collection<Component> components){

			List<String> matchesComponents = new ArrayList<>();

			for(Component c : components){

				//if there are no tags in this chart, proceed with name matching, old logic.
				if (componentTagsSet.isEmpty()) {
					if (isComponentMatches(c.getCategory(), c.getName(), c.getTags()))
						matchesComponents.add(c.getName());
				}else{
					//so tasks are required
					//if component has no tags skip it.
					if (c.getTags().size()==0){
						continue;
					}

					//check if component has a matching tag && name matches.
					boolean tagMatched = false;
					for (String tag : c.getTags()) {
						if (componentTagsSet.contains(tag))
							tagMatched = true;

					}
					if (tagMatched==true && isComponentMatches(c.getCategory(), c.getName(), c.getTags())){
							matchesComponents.add(c.getName());
					}

				}

			}

			return matchesComponents.toArray(new String[matchesComponents.size()]);

		}*/

		public String getCategoryName() {
			return categoryName;
		}

		public Pattern getNamePattern() {
			return namePattern;
		}
	}

	@Override
	public String toString() {
		return "ChartLineConfig{" +
				"component='" + component + '\'' +
				", accumulator='" + accumulator + '\'' +
				", caption='" + caption + '\'' +
				", componentTags='" + componentTags + '\'' +
				'}';
	}
}
