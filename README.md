# mvn-nexus-publisher

Tool for publishing project dependencies to local Nexus repository.

## Installation

1. Download source codes from http://example.com/FIXME.
2. Build project with command:

        $ lein uberjar


## Usage

Usage: mvn-nexus-publisher <classpath-file> <path-to-settings.xml> <repo-id> <repo-uri> <tmp-folder>
To create classpath file call: lein deps && lein classpath > deps.txt
settings.xml should be with user and password for given repo-id
tmp folder will be used to store temporary files.
In order to publish all project dependencies just call

    $ java -jar mvn-nexus-publisher-0.1.0-standalone.jar <classpath-file> <path-to-settings.xml> <repo-id> <repo-uri> <tmp-folder>
    
    Example:
    
    $java -jar /Users/mike/IdeaProjects/mvn-nexus-publisher/target/uberjar/mvn-nexus-publisher-0.1-standalone.jar ../myapp/deps.txt  /Users/mike/.m2/settings.xml thirdparty.repo http://nexus.sigma.myorg.com:8099/nexus/content/repositories/FMA_thirdparty /Users/mike/2

## License

Copyright © 2017 Mike Ananev

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
