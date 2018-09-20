[![Build Status](https://travis-ci.org/viritin/viritin.svg?branch=master)](https://travis-ci.org/viritin/viritin)
[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/viritin)
[![Stars on vaadin.com/directory](https://img.shields.io/vaadin-directory/star/viritin.svg)](https://vaadin.com/directory/component/viritin)

# Viritin - The "commons" library  for Vaadin

**Note, Attention, Alert for existing users**, Maddon was recently renamed to Viritin. New Maven coordinates: org.vaadin:viritin:1.22 (or whatever the latest version). After dependency change fixing imports in broken classes should do the thing. In addition to package rename, there is one potentially breaking change. EagerValidation is now default in AbstractForm.

#### [Documentation](https://github.com/viritin/viritin/wiki) project is going on in wiki

## Background

All large and stable libraries have their inconveniences and missing pieces. This is true for JDK libraries and just as true for Vaadin. Advanced Java users use various apache-commons libraries or Guava to fix these inconveniences in JDK libraries. This library tries to do the same for Vaadin.

The idea is to concentrate on server side Java to improvements to existing APIs, so that throwing this in to an applications or to another add-ons should be as easy as possible. Hopefully one can avoid some of stupid boilerplate code by using this library and make you more productive than ever with Vaadin.

So far improvements have been focused on three main categories:

 * Core component extensions that fix wrong defaults and provide more expressive API
 * Data binding improvements, better "forms" for both end users and developers
 * Essential new components build with server side composition (e.g. DisclosurePanel and fields to handle collections)
 
Some examples of improvements:

E.g. this very common Vaadin code...

```java
// Bind fields to entity properties by naming convention
BeanFieldGroup<Contact> binder = new BeanFieldGroup<Contact>(Contact.class);
binder.setBuffered(false);
binder.bindMemberFields(view);
binder.setItemDataSource(contact);
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

Or when using the "intelligent" expand method:

```java
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setMargin(true);
        wrapper.setSpacing(true);
        wrapper.setHeight("100%");
        HorizontalLayout toolbar = new HorizontalLayout(c, d);
        toolbar.setWidth("100%");
        toolbar.setMargin(false);
        toolbar.setSpacing(true);
        HorizontalLayout wrapper2 = new HorizontalLayout();
        wrapper2.addComponent(menu);
        wrapper2.addComponent(mainContent);
        wrapper2.setSizeFull();
        mainContent.setSizeFull();
        wrapper2.setExpandRatio(mainContent, 1);
        wrapper.addComponents(toolbar, wrapper2);
        wrapper.setExpandRatio(wrapper2, 1);
        setContent(wrapper);
```

Becomes...

```java
        setContent(new MVerticalLayout(new MHorizontalLayout(c, d).withFullWidth())
                .expand(new MHorizontalLayout(menu).expand(mainContent)));
```

Note that in the above the expand call does setExpandRation(component, 1), where 1 the value that people use 99% of the time, but it takes care of adding component and setting sane size values for the layout and the added component. 

The example is optimized for the number of lines, but most often you want to uses some line breaks and indentation for better readability. In anyways, the improved API lets you express the layout so that it is much easier to understand the hierarchy of component tree from the code, without adding lots of methods or classes.


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
t.addMValueChangeListener(new MValueChangeListener<Entity>() {
    @Override
    public void valueChange(MValueChangeEvent<Entity> event) {
        editEntity(event.getValue());
    }
});
```
Note, that in the above, the event actually provides you the value you are interested directly and in with the correct type. Especially for new Vaadin users this is very helpful.

Using Label extensions can make your life both simpler and safer:

```java
layout.addComponent(new Header("Tempertures during past week:"));
layout.addComponent(new RichText().withMarkDown("#How di hou [We have a link](https://vaadin.com/)!"));
layout.addComponent(new RichText().withMarkDown(getClass().getResourceAsStream("/readme.md")));
layout.addComponent(new RichText().withSafeHtml(getClass().getResourceAsStream("/readme.html")));
```

Hopefully all of these enhancements will end up in the core Vaadin library, in a one form or another. 

If you have a re-usable server side Vaadin "api-hack" that you continuously use in you project to reduce your madness, please contribute it. I promise to accept pull requests sooner and with lighter process than to the core Vaadin. 

Also feel free to suggest dependencies to new or possibly already existing add-ons that are similar in nature (pure server side, simple and smallish). This way you'll only need to add one helper dependency to get what you most often want to. If your dependency or addition is not small or simple, we can still consider if it is otherwise high quality (well tested, industry proven). Current dependencies:

 * ConfirmDialog (used by ConfirmButton in Viritin)

If a helper extends core Vaadin class, the convention is to simply prefix it with "M" letter. Otherwise, lets just use good naming conventions and reasonable sub packages.

## List of helpers 

Currently project contains following helpers (might be slightly outdated):

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
		* TypedSelect
			* A single select to another entity type
			* By default NativeSelect, but implementation is configurable
		* EnumSelect
			* a zero-conf select for enums (when used with FieldGroup), based on NativeSelect
   		* MTable extends Table (note that Table is often also the best real life listselect) 
			* single select only but better typed for generic bean usage 
      	  	* in addition to typing and related methods
         		* smart selection mode and immediate (based on value change listener, [Vaadin Ticket](http://dev.vaadin.com/ticket/8029) ) 
	     	   	* withProperties(String...), withColumnHeaders
		* MultiSelectTable
			* A Field suitable for editing varios collections of other entities (e.g. List<MyPojo> pojos), selected from a list given by developer.
			* Suitable for editing e.g. JPA ManyToMany mapped field
			* The field modifies the existing set/list, making if efficent and more compatible with your backend. The standard "multiselect mode" in core Vaadin selects always uses new Set and the value is always Set :-(
			* Works with both Set and List
* Element collection fields
	* Two implemenatations: ElementCollectionField (based on gridlayout) and ElementCollectionTable (based on Table)
	* A Vaadin field that edits a collection of embedded objects. E.g. OneToMany or ElementCollection annotated collection fields in JPA apps.
	* Example: Edits a List of Address objects in a Person object in an addressbook app
* Label extensions
 * Header to make headers without html hacking
 * RichText component for XSS safe html inclusion. Also supports Markdown syntax.
* Button extension
 * PrimaryButton (stylename + enter shortcut)
 * ConfirmButton (Customizeble confirm dialog before the actual listener is called)
* ListContainer (used by e.g. MTable, but can be used separately as well)
 * A lightweight replacement for BeanItemContainer that uses apache commons-beanutils for introspection. The performance (both memory and CPU) is also superior to the core Vaadin counterpart.
 * Note, that beans are kept in orinal list that developer used to pass beans. Wrap in e.g. ArrayList if you wish to keep original List unmodified.
* LazyList and SortableLazyList
 * Together with ListContainer makes memory and query efficient wiring from UI components like Table into your backend dead simple.
 * MTable supports the wiring directly with constructors, LazyComboBox has custom version that supports filtering done by ComboBox.
* DowndloadButton
 * Simplifies creating downloads for dynamically created resources, like PDF reports. Inverts the core Vaadin api to provide output stream for developer into where the content should be written. Also connects download extension to button automatically.
* AbstractForm
 * An abstract super class for simple entity editor. Provides save/cancel buttons and interfaces for hooking into them. Does binding with naming convention using BeanBinder when setEntity(T) is called. It is suggested to use the "setEagerValidation(true)" mode as it will make your forms much more UX friendly by default. This makes fields validating while you edit the form, instead of postponing it until the save click. Since 1.22 this is the default, but can be disabled if eager validation is for some reason not wanted. 
* DisclosurePanel is a simple panel whose content is hidden by default. Easy method to hide fields that are most often irrelevant for the end user.


## Online demo/usage examples

Online demo for this add-on hopefully makes no sense. But there are though many demo apps that contain helpful usage examples. Some of them listed here:

 * Add-ons own test sources, both [integration tests](https://github.com/viritin/viritin/tree/master/src/test/java/org/vaadin/viritin/it) and [unit tests](https://github.com/mstahv/maddon/tree/master/src/test/java/org/vaadin/viritin) contains some usage examples.
 * [Vaadin Java EE application example](https://hub.jazz.net/project/vaadin/vaadin-jpa-app/overview)
 * [Spring Data + Vaadin UI example](https://github.com/mstahv/spring-data-vaadin-crud)
 * Another [Java EE app example](https://github.com/mstahv/jpa-addressbook) - contains JPA usage examples for ManyToMany (MultiSelectTable) and ElementCollection (InlineEditableCollection).
 * Invoicing example [Java EE app](https://github.com/mstahv/jpa-invoicer) - contains JPA usage examples ElementCollection (InlineEditableCollection), LazyComboBox, DownloadButton.

### Usage examples

Run the following mvn command to start a small application
(http://localhost:9998) with all usage examples under `org.vaadin.viritin.it`.

```
mvn -e exec:java -Dexec.mainClass="org.vaadin.viritin.it.UiRunner" -Dexec.classpathScope=test
```

## Download a release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/viritin

## Maddon is now called Viritin

My colleagues teased me enough about the add-on name Maddon. M can mean Matti's add-on, but it also had to dual nature meaning I always made helpers there when APIs in core Vaadin (possibly even written by me) made me mad when using it :-)

Anyways, Viritin is a Finnish word, meaning tuner, and I think it describes the add-on pretty well too. It tunes Vaadin to be on the same frequency with you. English people might pronounce it almost like "wire it in" which also suits for the add-on, as lots of its latest helpers are related to binding your domain model to UI, wiring it in for the actual end user.

The add-on itself has been quite popular already. And there has bee lots of active contributors, thank you! There is now also a viritin organization in the github and I hope to get even more contributions for it in the future.

You can also expect some cool other Vaadin related things to pop up to under the governance of "Viritin organization". That yet to be published helper might change the way you use Vaadin even more than the Viritin add-on itself.

