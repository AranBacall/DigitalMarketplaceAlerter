# How to release

This project uses [Symantec Versioning](http://semver.org/) - this dictates what your version number will be.

## Prerequisites

You are on the Master branch, and are in sync with the remote repository.

```
git checkout master
git pull
```

There should be no pending changes on Master.


## Create a non-SNAPSHOT version

Change the pom.xml file to remove the "-SNAPSHOT" from the Version tags at the top.

Search and replace the README.md file to remove the -SNAPSHOT from the command line.

Commit this via
```
git add pom.xml README.md
git commit
```

Create an annotated git tag.
 
 ```
 git tag -a r1.0.12
 ```

Clean and build the fat jar.

```
mvn clean package
```

*Keep hold of the fat jar - you'll need it in a minute.*

Push the changes to the remote repository
```
git push
git push --tags
```

Edit the tag in the GitHub UI, and attach the fat jar to it. Hit the "Publish" button to make this the most recent release.

Congratulations, you have published a Release.

## Post-release

Bump the version numbers in pom.xml and README.md and add "-SNAPSHOT" to the end.
 
Commit and push so everyone else starts building the new version.

```
git add pom.xml README.md
git commit
git push
```
