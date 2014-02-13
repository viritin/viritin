# Maddon - The "commons" library  for Vaadin

This add-on tries to fix a number of small inconveniences that Vaadin users (read: web app developers) face daily. Vaadin is a old and big UI library, so it is no secret there are "some". And even though we can identify these pain points, fixing them isn't straightforward to keep reasonable backwards compatibility.

Luckily one can overcome large amount of them with some simple helper classes. Like what various Apache "commons" libraries do to Java, this library tries to do to Vaadin. You should try adding this add-on to your project when the Vaadin API is about to make you mad.

The idea is to concentrate on server side Java to improvements to existing APIs, so that throwing this in to an applications or to another add-ons should be as easy as possible. Hopefully one can avoid some of stupid boilerplate code by using this library.

E.g. this very common Vaadin code...

```java
// Bind fields to entity properties by naming convention
BeanFieldGroup<Contact> binder = new BeanFieldGroup<Contact>(Contact.class);
binder.setBuffered(false);
binder.bindMemberFields(view);
binder.setItemDataSource(contanct);
```

...becomes like this:

```java
BeanBinder.bind(contact, view)
```

And using basic layouts without this add-on...

```java
VerticalLayout verticalLayout = new VerticalLayout();
verticalLayout.setMargin(true);
verticalLayout.setSpacing(true);
verticalLayout.setHeight("100%");
HorizontalLayout horizontalLayout = new HorizontalLayout(c, d);
horizontalLayout.setWidth("100%");
horizontalLayout.setMargin(false);
horizontalLayout.setSpacing(true);
verticalLayout.addComponents(a, b, horizontalLayout);
setContent(verticalLayout);
```

... may start to feel rather mad, after using them with sane defaults and "fluent api":

```java
setContent(new MVerticalLayout().withFullHeight().with(a, b,
        new MHorizontalLayout(c, d).withFullWidth().withMargin(false)));
```

Using Table to select a row for editor may simplify from ...

```java
Table table = new Table();
BeanItemContainer<Entity> beanItemContainer = new BeanItemContainer<Entity>(Entity.class);
beanItemContainer.addAll(findBeans());
table.setContainerDataSource(beanItemContainer);
table.setVisibleColumns("property", "another");
table.setColumnHeaders("Property 1", "Second");
table.setSelectable(true);
table.setImmediate(true);
table.addValueChangeListener(new Property.ValueChangeListener() {
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        Entity entity = (Entity) event.getProperty().getValue();
        editEntity(entity);
    }
});
```

to...

```java
MTable<Entity> t = new MTable(findBeans())
        .withProperties("property", "another")
        .withColumnHeaders("Property 1", "Second");
t.addValueChangeListener(new MValueChangeListener<Entity>() {
    @Override
    public void valueChange(MValueChangeEvent<Entity> event) {
        editEntity(event.getValue());
    }
});
```

Using Label extensions can make your life both simpler and safer:

```java
layout.addComponent(new Header("Tempertures during past week:"));
layout.addComponent(new RichText().withMarkDown("#How di hou [We have a link](https://vaadin.com/)!"));
layout.addComponent(new RichText().withMarkDown(getClass().getResourceAsStream("/readme.md")));
layout.addComponent(new RichText().withSafeHtml(getClass().getResourceAsStream("/readme.html")));
```



Hopefully all of these enhancements will end up in the core Vaadin library, in a one form or another. 

If you have a re-usable server side Vaadin "api-hack" that you continuosly use in you project to reduce your madness, please contribute it. I promise to accept pull requests sooner and with lighter process than to the core Vaadin. 

Also feel free to suggest depenencies to new or possibly already existing add-ons that are similar in nature (pure server side, simple and smallish). This way you'll only need to add one helper depenency to get what you most often want to. If your dependency or addition is not small or simple, we can still consider if it is otherwise high quality (well tested, industry proven). Current dependencies:

 * ConfirmDialog


If a helper extends core Vaadin class, the convention is to simply prefix it with "M" letter. Otherwise, lets just use good naming conventions and reasonable sub packages.

## Helpers 

Currently project contains following helpers:

* BeanBinder, [Vaadin Ticket](http://dev.vaadin.com/ticket/13068)
	* Reduces the most common usage of BeanFieldGroup into oneliner
* layouts
	* Basic layouts have bad defaults (margin,spacing), corrected in these [Vaadin  Ticket](http://dev.vaadin.com/ticket/12966)
 	* "fluent api" for selected methods like, margin and spacing (withMargin(false))
 	* current implementations
   		* MVerticalLayout
   	 	* MHorizontalLayout
   	 	* MGridLayout
* MTextField
	* smart immediate handling (like what is coming in 7.2)
	* empty sting as null representation ([Vaadin Ticket](http://dev.vaadin.com/ticket/13221)) 
* Typed selects
	* Most developers use basic pojos as options and as field value. The cores Container-Item-Property with some legacy makes code look horrible in some cases. Thus these selects have
		* Generics for value/options
		* Has API for fluent usages with java.util.Collections
		* on TODO separate multi select implementations 
	* Current implementations (TODO lots of essential stuff needed here!!) 
   		* MBeanTable extends Table (note that Table is often also the best real life listselect) 
			* single select only but better typed for generic bean usage 
      	  	* in addition to typing and related methods
         		* smart selection mode and immediate (based on value change listener, [Vaadin Ticket](http://dev.vaadin.com/ticket/8029) ) 
	     	   	* withProperties(String...), withColumnHeaders
				


## Online demo

Online demo for this add-on hopefully makes no sense. Otherwise this add-on has lost its focus.


## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/maddon


## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- IDE that supports Maven

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

 
## Release notes

### Version 1.0-SNAPSHOT
- ...
- ...

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. That said, the following features are planned for upcoming releases:
- ...
- ...

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

MVerticalLayout is written by <...>

# Developer Guide

## Getting started

Here is a simple example on how to try out the add-on component:

<...>

For a more comprehensive example, see src/test/java/org/vaadin/template/demo/DemoUI.java

## Features

### Feature A

<...>

### Feature B

<...>

### Feature C

<...>

## API

MVerticalLayout JavaDoc is available online at <...>