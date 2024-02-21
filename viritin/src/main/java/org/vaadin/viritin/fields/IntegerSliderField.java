package org.vaadin.viritin.fields;

/**
 *
 * @author Matti Tahvonen
 */
public class IntegerSliderField extends IntegerField {

	private static final long serialVersionUID = -3019209950602573361L;

	private Integer max;
	private Integer min;
	private Integer step;

	public IntegerSliderField() {
    	tf.setPrimaryStyleName("v-widget v-slider");
		setHtmlFieldType("range");
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public IntegerSliderField withMax(Integer min) {
		setMax(min);
		return this;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public IntegerSliderField withMin(Integer min) {
		setMin(min);
		return this;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public IntegerSliderField withStep(Integer step) {
		setStep(step);
		return this;
	}

	@Override
	protected void configureHtmlElement() {
		super.configureHtmlElement();
		if (max != null) {
			s.setProperty("max", max.toString());
		}
		if (min != null) {
			s.setProperty("min", min.toString());
		}
		if (step != null) {
			s.setProperty("step", step.toString());
		}
	}

	@Override
	public IntegerSliderField withCaption(String caption) {
		return (IntegerSliderField) super.withCaption(caption);
	}
}
