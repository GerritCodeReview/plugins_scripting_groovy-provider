// Copyright (C) 2014 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.scripting.groovyprovider;

import com.google.common.collect.Sets;
import com.google.gerrit.extensions.annotations.Listen;
import com.google.gerrit.server.PluginUser;
import com.google.gerrit.server.plugins.InvalidPluginException;
import com.google.gerrit.server.plugins.ServerPlugin;
import com.google.gerrit.server.plugins.ServerPluginProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.eclipse.jgit.internal.storage.file.FileSnapshot;

import java.io.File;
import java.util.Set;

/**
 * Groovy scripting plugins.
 *
 * Allows to define a Groovy class to implement any type of Gerrit plugin.
 *
 * Example of Groovy SSH Plugin (hello-1.0.groovy):
 * ------------------------------------------------ import
 * com.google.gerrit.sshd.SshCommand import
 * com.google.gerrit.extensions.annotations.Export
 *
 * @Export("groovy")
 * class GroovyCommand extends SshCommand {
 *   public void run() {
 *   stdout.println("Hello Gerrit from Groovy !")
 * }
 *
 * The above example add a "hello groovy" command to Gerrit
 * SSH interface that displays "Hello Gerrit from Groovy !".
 */
@Listen
class GroovyPluginProvider implements ServerPluginProvider {
  private static final Set<String> GROOVY_EXTENSIONS = Sets.newHashSet(
      "groovy", "gvy", "gy", "gsh");

  private final Provider<GroovyPluginScriptEngine> scriptEngineProvider;

  @Inject
  public GroovyPluginProvider(Provider<GroovyPluginScriptEngine> scriptEngineProvider) {
    this.scriptEngineProvider = scriptEngineProvider;
  }


  @Override
  public ServerPlugin get(String name, File srcFile, PluginUser pluginUser,
      FileSnapshot snapshot, String pluginCanonicalWebUrl, File pluginDataDir)
      throws InvalidPluginException {
    GroovyPluginScriptEngine scriptEngine = scriptEngineProvider.get();
    return new ServerPlugin(getPluginName(srcFile), pluginCanonicalWebUrl,
        pluginUser, srcFile, snapshot, new GroovyPluginScanner(
            getPluginName(srcFile), scriptEngine,
            srcFile), pluginDataDir,
        scriptEngine.getGroovyClassLoader());
  }

  @Override
  public boolean handles(File srcFile) {
    String srcFileName = srcFile.getName();
    String scriptExtension =
        srcFileName.substring(srcFileName.lastIndexOf('.') + 1).toLowerCase();
    return GROOVY_EXTENSIONS.contains(scriptExtension.toLowerCase());
  }

  @Override
  public String getPluginName(File srcFile) {
    String srcFileName = srcFile.getName();
    int dashPos = srcFileName.lastIndexOf('-');
    int dotPos = srcFileName.lastIndexOf('.');
    return srcFileName.substring(0, dashPos > 0 ? dashPos:dotPos);
  }
}
