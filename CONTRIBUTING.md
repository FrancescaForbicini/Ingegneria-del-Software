# Contributing to this repository

## Motivation

This document contains the set of rules used in this development of this project.

## Java

While writing Java code, you should always follow the standard [naming convention](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html).

## Git

Commit messages should follow some best pratices:
- the subject line must be shorter than 50 character
- the subject line must be capitalized
- the subject line must be in "imperative mood"
- the subject line `X` should fit well in _If applied, this commit will `X`_

The commits should be small (e.g. contain few updates). This will enhance the review process making it more effective and efficient.

## Branches, Issues, and Pull Requests

Issues are action item, they must be well-defined and reviewed by all members of the team. They should reflect the development of the projects and the deadlines.
An issue can be a new feature to implement or a bugfix.

A branch should always start from the last version of `master`.
The branch names must follow this patterns:
- `feature/[FEATURE-NAME]`: for new features (e.g. `feature/preliminary-uml`)
- `bugfix/[FEATURE-NAME-BUG]`: for fixes to old features (e.g. `bugfix/preliminary-uml-typo`)

Pull Requests are the only way to write on the `master` branch, this aims to improve cooperation with an always tested and review ~master~ branch.
A branch(except `master`) is related to at most 1 pull request. After a PR is merged, the branch must be deleted.
A PR addresses 1 or more issues: the less the number, the easier the reviews and the integration.

In conclusion, the iteration for a feature (or a bugfix) should follow this steps:
- pick an issue
- switch to `master` (`git switch master`)
- fetch the remote (`git pull`)
- branch and move to `feature/your-feature` (`git switch -c feature/my-feature`)
- write some code
- commit
- re-fetch master (`git fetch origin`)
- rebase on master: if master was updated, this could cause conflict(`git rebase origin/master`)
- push `feature/your-feature` (`git push origin feature/your-feature`)
- open a pull request from GitHub
- ask for 2 reviews (the rest of the team)
- while (some changes are requested):
  - discuss requests or fix
- the PR has now 2x approvals and is ready to be merged on `master`
