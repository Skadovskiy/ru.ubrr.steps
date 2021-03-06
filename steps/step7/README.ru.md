# Шаг №7 - Нормализуем вывод при помощи логгирования

Нужно заменить инструкции System.out.println на полноценный вывод информации в лог, как это принято на реальных проектах.

## Как выполнять задание

Если Вы разрабатывали код предыдущих шагов в IntelliJ IDEA и установили плагин "Sonar Lint", то наверняка заметили, что он ругается на часто встречающуюся в нашем коде конструкцию `System.out.println`. Действительно, в программах на Java не принято выводить что-либо прямо на консоль - это считается неудобным.

Для вывода разнообразной информации в текстовом виде в процессе работы программы обычно используют такой инструмент, как "журналирование" или "логгирование" (мы будем в дальнейшем пользоваться именно последним термином, поскольку он более распространён в профессиональных кругах).

Причина, по которой вывод на консоль считается плохой практикой, состоит в том, что в процессе работы программы и от запуска к запуску сложно управлять тем, что выводится, а что - нет.

При этом это часто необходимо - например, в процессе отладки приложения необходимо бывает больше информации, а уже в процессе боевой эксплуатации вывод этой информации может замедлять работу программмы и поэтому нежелателен. `System.out.println` выводит переданный ему текст на консоль в любом случае, а механизм журналирования позволяет отдельно писать сообщения, указывая их уровень (см. ниже) и в отдельном месте при помощи настроек управлять тем, выводить ли сообщения с определёнными уровнями. Чтобы можно было оставить вывод только самых важных сообщений и не выводить менее важную, служебную информацию и для этого даже не нужно было бы перекомпилировать программу, а лишь поправить конфигурационные файлы и перезапустить программу.

Также можно отдельно настраивать то, куда эти сообщения выводить - в файл, на консоль, или куда-то ещё. Например, можно было бы всё важное и не важное писать в специальный файл (т.н. файл логов), а на консоль выводить только критически-важные сообщения. Или наоборот - на консоль выводить всё, а в файл писать только критически-важное.

### Библиотека (зависимость)

Собственно, необходимая для логгирования библиотека должна была быть уже подключена Вами на 2-м шаге. Напомним, о какой библиотеке шла речь:  

```xml
<project>
    <properties>
        <!--...-->
        <log4j-slf4j-impl.version>2.11.1</log4j-slf4j-impl.version>
    </properties>
         
    <!--...-->

    <dependencies>
        <!--...-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>${log4j-slf4j-impl.version}</version>
            <optional>true</optional>
        </dependency>
    </dependencies>
</project>
```

Собственно, убедитесь, что она подключена в POM'е и идём дальше.

### Конфигурируем логгирование

Создайте в вашем проекте папку `./src/main/resources`, если её ещё нет и скопируйте туда файл [log4j2.xml](https://github.com/hexlet-boilerplates/java-package/blob/master/src/main/resources/log4j2.xml) .

В этом файле конфигурируется логгирование по вашему проекту, давайте пройдёмся по нему, чтобы Вы понимали, зачем там те или иные элементы:
   * В файле два основных раздела:  
      1. `Appenders` - это механизмы, добавляющие записи к логу. В данном случае у нас два аппендера:
         * `Console` - очевидно, консоль, с которой мы пока и работаем
         * `RollingFile` - файлы, располагающиеся в директории `logs`, формат имён которых мы задаём параметром `filePattern`
            - для ясности поправьте в 9-й и 10-й строках имена так, чтобы они совпадали с именем вашего проекта вместо `java-package`  
      2. `Loggers` - это адресаты log-записей. Ими обычно выступают файлы и консоль, но могут быть и базы данных, и сетевые соединения.   

Среди настроечных параметров фигурируют т.н. уровни логгирования. Их 6; перечислим их по убыванию важности:  
   1. **FATAL** - сообщение о проблеме, несовместимой с продолжением работы программы. Если сопоставлять работающую программу с живым человеком, то это сообщение - последний стон умирающего.  
   2. **ERROR** - сообщение о проблеме, которая совместима с продолжением работы программы, но означает невозможность выполнения каких-то важных функций. Для человека это было бы что-то серьёзное, но всё-таки не смертельное, например, утрата ноги или руки.  
   3. **WARN** - сообщение о проблеме, для которой был предусмотрен резервный вариант, страховка, которая сработала и система отработала как нужно, однако разработчику или администратору разорбраться всё же стоит, потому что в следующий раз так может не повезти.  
   4. **INFO** - нейтральное сообщение - обычно это отчёт о том, что в каком-то процессе успешно прошла очередная фаза.  
   5. **DEBUG** - сообщение, которое может представлять интерес для разработчика системы, если он решит её отлаживать.  
   6. **TRACE** - вся прочая, чаще всего ненужная информация  

### Подключаем logger к классу

`logger` (чаще сокращённо - `log`) - это объект, который позволяет Вам осуществлять логгирование. Форма его создания достаточно стандартна:

```java
class ClassName {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClassName.class);
    // остальной код класса
}
```

Имя класса должно соответствовать имени класса, в котором это статическое поле объявляется.

После этого можете вызывать методы с использованием точечной нотации. Имена методов совпадают с перечисленными выше уровнями логгирования.

Замените операцию вывода всех сообщений `System.out.println` на вызовы `log.info` во всех методах и классах проекта. Посмотрите на результат в консоли. После чего загляните в папку `logs` и посмотрите сообщения там.

Папку `logs` необходимо за-`.gitignore`'ить, чтобы не засорять ими репозиторий на GitHub'е.

### Тюнинг формата сообщений

Для вывода сообщений в Appender'ах используется достаточно простой формат. Он задаётся в теге `PatternLayout`, в параметре `pattern`. Измените его так, чтобы на консоль не выводилась ненужная, лишняя информация - только уровень логгирования и само сообщение, тем не менее лог-файл был бы полон.

Если всё работает корректно, значит Вы выволнили все задания проекта - поздравляем!

Не забудьте закоммитить, запушить, отметить этот шаг как выполненный и ждите результатов проверки!
