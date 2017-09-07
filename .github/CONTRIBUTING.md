# Contribution guidelines

You want to contribute? Brilliant! Let me first of all thank you very much for concidering contributing to this project. :smiley:

1. [General procedure](#General-procedure)
2. [Working on an issue](#Working-on-an-issue)
3. [Working on something that doesn't have an issue](#Working-on-something-that-doesn't-have-an-issue)
4. [Code style](#Code-style)
5. [Commit messages](#Commit-messages)
6. [Pull requests](#Pull-requests)

## General procedure

1. Choose what you want to work on.
2. Does an issue already exist for this? If yes write a comment indicating that you want to solve it. If not concider opening one first.
3. Fork the project.
4. Work on what you wanted to work on. This includes working on the following aspects.
    a. The code itself.
    b. Javadocs for the code as a minimum.
    c. Maybe a wiki page explaining particularly complex aspects of your code (optional).
    d. Unit tests.
    e. Try not to mix things you work on. If you must work on multiple things at the same time try to split them into different branches and different pull requests to keep things clean.
5. Clean up your git history if necessary. More on this in [Commit messages](#Commit-messages).
6. Open a pull request on the `develop` (!) branch.
7. Wait for the automatic travis build to complete without errors.
8. Wait for a maintainer to review your code.
9. If necessary make changes to your code as requested by the maintainer.
10. Whatch your code be merged and eventually be released into the wild.

## Working on an issue

If you want to work on something that already has an open issue please leave a commit on the relevant issue to indicate that you are working on this and want to claim the issue. Usually a maintainer will then assign that issue to you. This is meant to avoid multiple people handling the same issue. No one likes to waste their time by realizing someone already fixed it when you were almost done.

## Working on something that doesn't have an issue

It is highly recomended to open an issue *before* you start working on anything. Just make sure to clearly state that you want to work on this issue. However the maintainers of this project have the right to let you know that this feature will not be implemented i. e. not be merged if you implement it should that feature not be in line with the goals of the project. Once you opened an issue everything from [Working on an issue](#Working-on-an-issue) applies.

If you want to work on something in secret and surprise everyone with a pull request of an awesome finished feature out of nowhere you are welcome to do so. However be advised that some risks are involved with this:

- Someone may be doing the same thing as you are and be just that bit quicker that your work becomes obsolete.
- Someone may open an issue with virtually the same feature as what you are working on. In that case keep an eye on the issue tracker and just be quick at claiming the issue.
- The feature you are developing may not be in line with the maintainers ideas of the project goals. This would lead to a rejection of the pull request and lots of wasted time.

## Code style

The code style is pretty much the default Java code style. Just take a look at the existing code. Here just some key notes:

- Opening brackets in the same line as the previous statement.
- Closing bracket in their own line, except on `} else {` do-while `} while(...);` and catch statements `} catch(...) {`.
- Camel case with classes beginning with an upper case and methods, functions, variables and members beginning with a lower case character.
- `public static final`s i. e. constants as all capitals with snake case `public static final boolean LIKE_THIS = true;`.
- Usage of Java streams and lambda expressions is very much encouraged.

## Commit messages

- Commit messages should be reasonably descriptive. A summary is mandatory (max. 50 characters). Do *not* exceed the 50 charactes. If you feel like you need to say more keep the summary short and (after the blank line) add a long description of the commit.
- The longer description is not mandatory but encouraged if your commit contains something complex.
- Several small commits are prefered over some large commits. Try to do *one thing* in each commit.
- If you just wanted a quick and dirty commit to save your progress that is fine but please clean up by rewriting history before opening your pull request. Use `git rebase -i HEAD~n` for this with `n` being the number of commits you want to go back. [This article](https://git-scm.com/book/id/v2/Git-Tools-Rewriting-History) describes this procedure in detail.

## Pull requests

Once you completed your work and cleaned up your commit please open a pull request. The message of that pull request should at least contain a reference to the issue you were working on and to what extend you actually solved the issue. Partial sollutions are perfectly fine if you feel like you can't or don't want to spend more time on this.

If the pull request is a surprise without an issue please use the [issue template](https://github.com/targodan/UberSpatchBoard/blob/master/.github/ISSUE_TEMPLATE.md) to create your pull request. Adding the above mentioned information additionally to the info about the issue.

Please be aware though that just because you submitted a pull request does not necessarily mean your pull request will be merged immediately or even at all.
A maintainer may ask you to take additional actions before they will merge your request.

Possible reasons for you to need to take action:
- Automatic travis build is unsuccessful. In this case you should take action before being asked to do so. Maintainers will typically not review your code before the build succeeds.
- Wrong target branch.
- Fork can't be auto merged (e. g. because it is to heavily out of date). A maintainer may ask you to rebase your fork or to merge the `develop` branch from this project into your fork in order to fix this.
- Your code did not pass a maintainers review for one of the following reasons.
    - Unclean commits.
    - Not complying to code style
    - Missing or insufficient tests
    - Missing or insufficient javadocs
    - Cluttered or badly written code
    - Bad design (This is likely to be rare and the maintainer will make suggestions on how to improve the code design)

**One important thing:** Please do not take it personally if we ask you to improve your code or if your pull request is rejected. We want this project to flourish. This includes keeping it maintainable. Bad software design or cluttered code will reduce the maintainability, hence we cannot allow such code to be merged. We will however do our best to give suggestions and to help you improve your code so it can eventually be merged.

## Become a comaintainer

You want to become a comaintainer? Great! :smiley:

Please be aware of your duties as a maintainer of this project:
- Keep an eye on the issue tracker.
- Moderate issues according to the [Code of Conduct](https://github.com/targodan/UberSpatchBoard/blob/master/CODE_OF_CONDUCT.md)
- React to new issues
    - Communicate with the issuer
    - Try to analyze the problem/feature submitted and clear up any ambiguities or possible misunderstandings
- Manage pull requests
    - Review code
    - Communicate with the developer who opened the pull request to keep the project code at a high level of quality without insulting them.

There are of course also benefits:
- You get a say in what is developed at what time/until which release.
- Direct access to the `develop` branch, no need for pull requests.

Requirements to become a maintainer:
- Being able to understand, read and write in english (basic levels + programming specific terminology are sufficient)
- Being a dispatch drilled fuelrat. This is a dispatch aid after all. (Not necessary for merely contributing)
- Experience in Java and with git
- Read and understand [SemVer](http://semver.org/)
- Read and understand [git-flow](https://danielkummer.github.io/git-flow-cheatsheet/)
- Open an issue with the title "Comaintainer application YOURNAME" (The github username is sufficient). It should include the following
    - A little information about yourself. How long you've been programming for, what's your favourite programming language and so on. Real name is not necessary.
    - Links to some of your projects (optional)
    - Your fuelrat name (so I can verify that you are dispatch drilled)
    - The nickname you use most in the fuelrats IRC (so I can verfy your github nickname)
