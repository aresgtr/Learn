# Setter or Constructor

## Setter or Constructor Based DI
- Since you can mix constructor-based and setter-based DI, it is a good rule of thumb to use __constructors__ for mandatory dependencies and setter methods for optional dependencies.
- The Spring team generally advocates constructor injection as it enables one to implement application components as immutable objects and to ensure that required dependencies are not null.
- Furthermore constructor-injected components are always returned to the client (calling) code in a fully initialized state.
- As a side note, a large number of constructor arguments is considered bad practice (3 args should be max).
- A large number of constructor arguments implies that the class likely has too many responsibilities and should be refactored to better address proper separation of concerns.
- Setter injection should primarily only be used for optional dependencies that can be assigned resonable default values within the class.
- Otherwise, not-null checks must be performed everywhere the code uses the dependency.
- One benefit of setter injection is that setter methods make objects of that class amenable to reconfiguration or re-inject later.
- Use the DI style that makes the most sense for a specific class. Sometimes, when dealing with 3rd party classes the choice is made for you.
- For example, if a 3rd party class does not expose any setter methods, then constructor injection may be the only available form of DI.

## Dependency Resolution Process

The container performs bean dependency resolution as follows:

- The _ApplicationContext_ is created and initialized with configuration metadata that describes all the beans (configuration can be implemented using XML or with Annotations via Java code)
- For each bean, its dependencies are expressed in the form of properties or constructor arguments
- These dependencies are provided to the bean, when the bean is actually created
- Each property or construtor argument is an actual definition of the value to set, or a reference to another bean in the container (we used the _ref_ attribute in xml)
- Each property or constructor argument which is a value is converted from its specific format to the actual type of that property or constructor argument.
- By default Spring can convert a value supplied in string format to all built-in types, such as int, long, String, boolean, etc.
- The Spring container always validates the configuration of each bean as the container is created.
- However, the bean properties themselves are not set until the bean is actually created.
- The creation of a bean potemtially causes a graph of beans to be created, as the bean's dependencies and its dependencies's dependencies (and so on) are created and assigned.

## Circular Dependencies
- If you predominantly use constructor injection, it is possible to create an unresolvable circular dependency scenario.
- For example: Class A requires an instance of class B through constructor injection, and class B requires an instance of class A through constructor injection.
- If you configure beans for class A and B to be injected into each other, the Spring container detects this circular reference at runtime, and throws a __BeanCurrentlyInCreationException__.
- One possible solution is to use setter-based injection for one of the classes rather than constructor-based injection. Alternatively, you can avoid constructor injection and use setter injection only.
- In other words, although it is __not recommended__, you can configure circular dependencies with setter injection.
- Unlike the typical case (with no circular dependencies), a circular dependency between bean A and bean B will force one of the beans to be injected into the other prior to being fully initialized itself (this is a classic chicken/egg scenario).