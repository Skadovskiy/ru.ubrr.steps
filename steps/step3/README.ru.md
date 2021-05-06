# Шаг №3 - Настройка механизма публикации и проверок

## Что должно получиться

Чтобы закончить подготовительную часть и приступить, наконец, к кодированию, нам необходимо сделать ещё несколько манипуляций, направленных на:
   * проверку того, собирается ли проект на TravisCI
   * публикацию на JitPack.io
   * подключение linter\`ов:
      - SpotBugs
      - PMD
      - SonarQube
      - CheckStyle

## Как выполнять задание

### Публикуем проект при помощи Jitpack.io

Пропишем настройки для возможности выкачивать нашу библиотеку как зависимость в других проектах при помощи сервиса `JitPack.io`

Для того, чтобы написанный нами код могли использовать другие разработчики в своих проектах, в Java-мире принято делать его доступным для подгрузки тем же способом, при помощи которого мы так же подтянули такие зависимости, как _Slf4J_ и _Apache Commons Lang_ - т.е. посредством Build-tool\`ов, таких как Maven и Gradle.

При этом если Вы считаете, что эта библиотека действительно нужна людям, тогда место ей - в Maven Central\`е. Но если библиотека пока не представляет такого интереса, но тем не менее Вы допускаете, что она может кому-то понадобиться, то есть вариант попроще - сервис [JitPack.io](https://jitpack.io/)

Этот сервис при первом запросе на ресурс выкачивает ваш проект и собирает его при помощи того Build-tool\`а, который в нём найдёт. Для того, чтобы дать ему понять, как именно нужно собирать Ваш проект, ему нужно передать некоторые настройки и по соглашению это делается в файле `jitpack.yml`.

Пока нас устроят все настройки по умолчанию этого сервиса, кроме одной - версия Java в нём по-умолчанию 8`я, а мы договорились использовать 11-ю.

Так что давайте пропишем в этот файл пару строчек, чтобы сориентировать сервис на сборку на 11-й Java-версии:

```yaml
jdk:
  - openjdk11
```

Всё! После этого наш проект должен выкачиваться после указания в качестве зависимости в других проектах.

Для этого им при использовании Maven\`а, к примеру, нужно будет сделать 2 вещи:
   1. Прописать jitpack.io в качестве репозитория:  
      ```xml
      <repositories>
          <repository>
              <id>jitpack.io</id>
              <url>https://jitpack.io</url>
          </repository>
      </repositories>
      ```

   2. Указать зависимость:
      ```xml
      <dependency>
          <groupId>com.github.{Ваш-пользователь}</groupId>
          <artifactId>{имя-репозитория-проекта}</artifactId>
          <version>{ветка(например,"-MASTER")}</version>
      </dependency>
      ```

Создайте пустой Maven-проект и, используя эту информацию, подтяните в качестве внешней зависимости данный, чтобы убедиться, что всё работает.

Имейте в виду, что посмотреть на информацию по сборке вашего проекта можно, зайдя под своей учёткой GitHub`а на сайт jitpack.io и посмотрев report.

Также имейте в виду, что у вас будет лишь одна попытка - если сборка проекта прошла неудачно, следующая осуществляется лишь для релиза. Поэтому если что-то не получится - придётся создавать релиз средствами GitHub\`а и только после этого проверять сборку в следующий раз.

Разместите также бейдж в файле ./README.md своего проекта. Для этого, зайдя на сайте jitpack.io под своим github-аккаунтом, введите название своего проекта, нажмите "Look up", выберите вкладку "Branches" и рядом с интересующей вас веткой нажмите в колонке Status кнопку "Get it". После перехода найдите надпись "Paste this in your README.md to add a badge", скопируйте и вставьте себе в README.md текст ссылки под этой надписью. Запуште, сделайте релиз и убедитесь, что бейдж появился и он зелёный.

### Проверяем, собирается ли проект с помощью TravisCI

Попытайтесь прикрутить самостоятельно, опираясь на руководства:  
   * https://dzone.com/articles/travis-ci-tutorial-java-projects
   * https://rtfm.co.ua/travis-maven-bild-java-prilozheniya-i-deploj-na-azure/

Не забудьте так же разместить бейдж.

### Подключаем проверку на ошибки при помощи SpotBugs

Попытайтесь прикрутить самостоятельно, опираясь на руководства:  
   * https://spotbugs.github.io/
   * https://spotbugs.github.io/spotbugs-maven-plugin/

### Подключаем проверку на ошибки при помощи PMD

Попытайтесь прикрутить самостоятельно, опираясь на руководства:  
   * https://pmd.github.io/pmd/pmd_userdocs_tools_maven.html
   * http://maven.apache.org/plugins/maven-pmd-plugin/

### Подключаем проверку на ошибки при помощи SonarQube

Попытайтесь прикрутить самостоятельно, опираясь на руководство:  
   * http://www.mojohaus.org/sonar-maven-plugin/plugin-info.html

Учтите, что начиная с версии 3.\* плагин работает не корректно, поэтому последняя версия, которая Вам нужна - `2.7.1` .
Это проблематично, потому что плохо согласуется с политикой обновления версий, которую мы провели в конце предыдущего шага - при первом же выполнении команды `make update` вы получите последнюю версию этого плагина.
Для того, чтобы решить эту проблему, служит файл `maven-version-rules.xml`:  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ruleset xmlns="http://mojo.codehaus.org/versions-maven-plugin/rule/2.0.0"
         comparisonMethod="maven"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
                 http://mojo.codehaus.org/versions-maven-plugin/rule/2.0.0
                 http://www.mojohaus.org/versions-maven-plugin/xsd/rule-2.0.0.xsd">
    <rules>
        <rule groupId="org.codehaus.mojo"
              artifactId="sonar-maven-plugin"
              comparisonMethod="maven">
            <ignoreVersions>
                <ignoreVersion type="regex">3.*</ignoreVersion>
            </ignoreVersions>
        </rule>
    </rules>
</ruleset>
```

Чтобы дать понять плагину `versions-maven-plugin` ориентироваться на эти ограничения, необходимо описать этот плагин в нашем pom.xml\`е явно:
```xml
<plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>versions-maven-plugin</artifactId>
        <configuration>
            <generateBackupPoms>false</generateBackupPoms>
            <rulesUri>file://${project.basedir}/maven-version-rules.xml</rulesUri>
        </configuration>
</plugin>
```

#### Plugin\`ы к IDEA

Если Вы используете IDEA, рекомендуется так же поставить соответствующие плагины:  
   * https://www.sonarlint.org/intellij/
   * https://github.com/vektory79/JenkinsArticles/blob/master/SonarQubeAndIDEA/SonarQubeAndIDEA.md

Так Вы сможете отслеживать ошибки ещё на стадии написания кода, не дожидаясь сборки проекта.

### Подключаем проверку на ошибки при помощи CheckStyle

Попытайтесь прикрутить самостоятельно, опираясь на руководство:
   * https://ru.stackoverflow.com/questions/607043/Тестирование-кода-в-travis-ci
   * http://maven.apache.org/plugins/maven-checkstyle-plugin/

#### Plugin\`ы к IDEA

Если Вы используете IDEA, рекомендуется так же поставить соответствующий плагин:
https://habr.com/sandbox/78503/
https://plugins.jetbrains.com/plugin/1065-checkstyle-idea

Поздравляем! Подготовка завершена, теперь комитте и пуште всё, что сделали, немного передохните и приступайте к следующему шагу!
