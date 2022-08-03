# Code review policy
## Basics
## Policy
- **Review over codding!** Review PR within "n" hours after its creation. Makes PR owners stay in the context of their work.
## Reviewing strategies

<details>
  <summary><h3>Per file</h3></summary>
  <b>Overview</b>
  Review a PR file by file. Can be performed in Github UI. Suits small PRs.
  <b>Advantages</b>
  Time efficient
  <b>Disadvantages</b>
  Gives poor task context
  Risk to miss errors
</details>
<details>
  <summary><h3>Per commit</h3></summary>
  <b>Overview</b>
  Review by commit history
  <b>Advantages</b>
  Gives more context to task
  Requires not much time
  <b>Disadvantages</b>
  Requires clear commit history
</details>
<details>
  <summary><h3>Check out locally</h3></summary>
  <b>Overview</b>
  Pull code and review it locally. <code>git merge --no-commit --no-ff <merge_candidate_branch></code>
  <b>Advantages</b>
  Gives the most context possible
  Allows to test your suggestions
  Allows to run code
  Supports referencing (easier code navigation)
  The whole file is visible against the changes area in Github UI
  <b>Disadvantages</b>
  Time-consuming
  Effort-consuming
  Requires to <code>stash</code> changes to your current task
</details>

## Checklist


<details>
  <summary><h2>Tips</h2></summary>
</details>

## Points for consideration
