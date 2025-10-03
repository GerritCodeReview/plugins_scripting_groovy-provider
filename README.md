# DEPRECATION NOTICE

GerritForge has decided to [change the license to BSL](https://gitenterprise.me/2025/09/30/re-licensing-gerritforge-plugins-welcome-to-gerrit-enterprise/)
therefore the Apache 2.0 version of this plugin is deprecated.
The recommended version of the groovy-provider plugin is on [GitHub](https://github.com/GerritForge/groovy-provider)
and the development continues on [GerritHub.io](https://review.gerrithub.io/admin/repos/GerritForge/groovy-provider,general).

Gerrit Groovy Provider Plugin (DEPRECATED)
=============================

This plugin provides Groovy runtime environment for Gerrit plugins in Groovy.

To build, link this directory under Gerrit's tree plugins directory, merge
the dependencies from external_plugin_deps.bzl into the file of the same
name in the plugins directory, and from the root of the gerrit tree run:

```
  bazel build plugins/groovy-provider
```

The resulting artifact can be found under:

```
 bazel-bin/plugins/groovy-provider/groovy-provider.jar
```

To test deploy the review plugin [1] and copy this Groovy Provider plugin
under `$gerrit_site/plugins` directory.

Review plugin in Groovy can be used:

```
  ssh gerrit review approve I59302cbb
  Approve change: I59302cbb
```

* [1] https://github.com/davido/gerrit-groovy-plugin
