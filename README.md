# quick-start
Maven archetype for quick-start projects

# Usage

Create a new maven project:
```
mvn archetype:generate -DarchetypeGroupId=com.a9ski.archetypes -DarchetypeArtifactId=quick-start
```

# Development guide

## Pre-commit
1. Install pre-commit (https://pre-commit.com/)
2. Install the pre-commit hook by executing `pre-commit install` inside project directory
3. Run against all files in the project: `pre-commit run --all-files`

## Github CLI
`brew install gh`


# Releasing a new version

## 1. Execute maven release plugin
```
mvn release:prepare
```
it will perform following steps:
- Check that there are no uncommitted changes in the sources
- Check that there are no SNAPSHOT dependencies
- Change the version in the POMs from x-SNAPSHOT to a new version (you will be prompted for the versions to use)
- Transform the SCM information in the POM to include the final destination of the tag
- Run the project tests against the modified POMs to confirm everything is in working order
- Commit the modified POMs
- Tag the code in the SCM with a version name.
- Bump the version in the POMs to a new value y-SNAPSHOT (these values will also be prompted for)
- Commit the modified POMs

## 2. Create a new release and trigger the GitHub action to publish to maven central repository.
```
TAG=$(cat release.properties | grep scm.tag= | sed s/scm.tag=//g) && gh release create $TAG --title $TAG --notes-file release-notes/$TAG.txt
```

## 3. Cleanup maven release descriptor and POM backups
```
mvn release:clean
```

to delete release descriptor and POM backups.

## 4. Release the repository from nexus
Go to https://oss.sonatype.org/#stagingRepositories and find the repository. Next choose to close. After being closed click the release button.

## 5. Reverting a release
If the `release.properties` file is present
```
gh release delete v1.0.0
mvn release:rollback
git rebase -i HEAD~4 # squash the release commits
git push --force
```


If the `release.properties` file is missing
```
mvn release:clean
gh release delete v1.0.0
git tag -d v1.0.0
git push --delete origin v1.0.0
#!!! Change pom.xml and decrement the version number
git commit --amend
git rebase -i HEAD~3 # squash the release commits
git push --force
```
