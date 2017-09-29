# mvn-nexus-publisher

Tool for publishing project dependencies to local Nexus repository.

## Installation

1. Download source codes from https://github.com/middlesphere/mvn-nexus-publisher
2. Build project with command:

        $ lein uberjar


## Usage

Call syntax:

```mvn-nexus-publisher <classpath-file> <path-to-settings.xml> <repo-id> <repo-uri> <tmp-folder>```

1. To create classpath file call: lein deps && lein classpath > deps.txt

2. settings.xml should be with user and password for given repo-id
3. tmp folder will be used to store temporary files.
4. Be sure that maven (mvn) is installed and available from command line (in PATH$)
5. In order to publish all project dependencies just call

```$ java -jar mvn-nexus-publisher-0.1.0-standalone.jar <classpath-file> <path-to-settings.xml> <repo-id> <repo-uri> <tmp-folder>```


Example:
    
    $java -jar /Users/mike/IdeaProjects/mvn-nexus-publisher/target/uberjar/mvn-nexus-publisher-0.1-standalone.jar ../myapp/deps.txt  /Users/mike/.m2/settings.xml thirdparty.repo http://nexus.myorg.com:8099/nexus/content/repositories/FMA_thirdparty /Users/mike/2

## License

Copyright © 2017 Mike Ananev

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
