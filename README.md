# Viritin - The "commons" library  for Vaadin

*Note, Attention, Alert*, Maddon was recently renamed to Viritin. New Maven coordinates: org.vaadin:viritin:1.22 (or whatever the latest version). After dependency change fixing imports in broken classes should do the thing.

All large and stable libraries have their inconveniences and missing pieces. This is true for JDK libraries and just as true for Vaadin. Advanced Java users use various apache-commons libraries or Guava to fix these inconveniences in JDK libraries. This library tries to do the same for Vaadin.

The idea is to concentrate on server side Java to improvements to existing APIs, so that throwing this in to an applications or to another add-ons should be as easy as possible. Hopefully one can avoid some of stupid boilerplate code by using this library and make you more productive than ever with Vaadin.

So far improvements have been focused on three main categories:

 * Core component extensions that fix wrong defaults and provide more expressive API
 * Data binding improvements
 * Essential new components build with server side composition (e.g. DisclosurePanel and fields to handle collections)
 
Some examples of improvements:

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
t.addMValueChangeListener(new MValueChangeListener<Entity>() {
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

If you have a re-usable server side Vaadin "api-hack" that you continuously use in you project to reduce your madness, please contribute it. I promise to accept pull requests sooner and with lighter process than to the core Vaadin. 

Also feel free to suggest dependencies to new or possibly already existing add-ons that are similar in nature (pure server side, simple and smallish). This way you'll only need to add one helper dependency to get what you most often want to. If your dependency or addition is not small or simple, we can still consider if it is otherwise high quality (well tested, industry proven). Current dependencies:

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
* AbstractForm
 * An abstract super class for simple entity editor. Provides save/cancel buttons and interfaces for hooking into them. Does binding with naming convention using BeanBinder when setEntity(T) is called. It is suggested to use the "setEagerValidation(true)" mode as it will make your forms much more UX friendly by default. This makes fields validating while you edit the form, instead of postponing it until the save click.
* DisclosurePanel is a simple panel whose content is hidden by default. Easy method to hide fields that are most often irrelevant for the end user.


## Online demo/usage examples

Online demo for this add-on hopefully makes no sense. Otherwise this add-on has lost its focus. There are though many demo apps that contain good usage examples. Some of them listed here:

 * Add-ons test sources, both [integration tests](https://github.com/mstahv/viriting/blob/master/src/test/java/org/vaadin/viritin/it/EditPerson.java) and [unit tests](https://github.com/mstahv/maddon/tree/master/src/test/java/org/vaadin/viritin) contains some usage examples.
 * [Vaadin Java EE application example](https://hub.jazz.net/project/vaadin/vaadin-jpa-app/overview)
 * [Spring Data + Vaadin UI example](https://github.com/mstahv/spring-data-vaadin-crud)
 * Another [Java EE app example](https://github.com/mstahv/vaadin-java-ee-essentials-example/tree/viewmenujpa) - contains JPA usage examples for ManyToMany (MultiSelectTable) and ElementCollection (InlineEditableCollection).


## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/viritin

