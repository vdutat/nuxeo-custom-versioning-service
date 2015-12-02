# About feature

Just before saving a document in the document repository, the `versioning service` calls a piece of code that could change the lifecycle state of the document. i.e. when a `File` document in state `approved` is modified, its state is put back to `project` by following the transition `backToProject`, this is the standard behaviour for a document having the lifecycle policy `default`.

But when you are using custom document types having their own custom lifecyle policies, this behaviour makes no sense, as the transition `backToProject` may not exist.

This behaviour can be changed by contributing a `Java` class to [extension point `versioningService` of service `VersionService`](http://explorer.nuxeo.com/nuxeo/site/distribution/Nuxeo%20Platform-6.0/viewExtensionPoint/org.nuxeo.ecm.core.versioning.VersioningService--versioningService)

This module contributes such a `Java` class, and allows you to specify what transition to follow by contribution some properties to [extension point `configuration` of service `ConfigurationService`](http://explorer.nuxeo.com/nuxeo/site/distribution/current/viewExtensionPoint/org.nuxeo.runtime.ConfigurationService--configuration).

i.e. a document of type `docType1` with a lifecycle policy `lcPolicy1` must follow transition `backToDraft` when it is modified and has the lifecycle state `validated`, here is the `XML` to put in a file named `ConfigVersioningService-config.xml` in directory `$NUXEO_HOME/nxserver/config/`:

```
<component name="org.nuxeo.ecm.versioning.ConfigVersioningService.configuration" >

  <extension point="configuration" target="org.nuxeo.runtime.ConfigurationService">
    <property name="nuxeo.versioning.versioningservice.lcPolicy1.from.validated.followTransition">backToDraft</property>
  </extension>
</component>
```

If you don't contribute any configuration to service `ConfigurationService`, the standard behaviour (following the transition `backToProject`) is enabled **only** for documents with lifecyle policy `default`, no change of lifecyle state will be done on documents having any other lifecycle policy.

This module is **not** supported by Nuxeo.

* Project status: prototype
 
# Building
 
```
mvn clean install
```

## QA
  
None.
 
## Deploying

2 options:

- copy the JAR bundle in `$NUXEO_HOME/templates/custom/bundles/` and activate the `custom` template (see Nuxeo documentation [Configuration Templates](https://doc.nuxeo.com/display/ADMINDOC/Configuration+Templates)).
- **OR** copy the JAR bundle in `$NUXEO_HOME/nxserver/plugins/` or `$NUXEO_HOME/nxserver/bundles/`

Then restart the server.

