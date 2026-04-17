# mod-cli-configured-style-testing

Minimal Gradle Java project that proves `mod config build style openrewrite edit`
actually causes the configured style to be honored by recipes like `OrderImports`.

## What this project contains

- `StarImportUserA.java`, `StarImportUserB.java` — both use `import java.util.*`.
  These files bias OpenRewrite's autodetected `ImportLayoutStyle` toward a low
  `classCountToUseStarImport` threshold (typically 5).
- `Target.java` — has **exactly 5 explicit `java.util` imports**. Under the
  autodetected threshold these would collapse into `import java.util.*;` when
  `OrderImports` runs.
- `no-star-imports.yml` — an OpenRewrite style named `abc.no-star-imports` that
  sets `classCountToUseStarImport: 999` and `nameCountToUseStarImport: 999`, so
  nothing ever collapses into a star import.

## Prerequisites

- Moderne CLI 4.1.3 or later. `mod --version` to check.

## Test A — no configured style → imports collapse (the problem)

```bash
# start clean
rm -rf .moderne

# confirm no OpenRewrite style is configured
mod config build style openrewrite show --local .

# build and order imports
mod build .
mod run . --recipe=org.openrewrite.java.OrderImports

# observe: Target.java's five java.util imports collapsed into "import java.util.*;"
git diff src/main/java/com/example/Target.java
```

Expected diff on `Target.java`:

```diff
-import java.util.ArrayList;
-import java.util.HashMap;
-import java.util.HashSet;
-import java.util.LinkedList;
-import java.util.List;
+import java.util.*;
```

Reset for the next test:

```bash
git checkout -- src/main/java/com/example/Target.java
```

## Test B — with configured style → imports stay expanded (the fix)

```bash
# point the CLI at the style file (writes to .moderne/moderne-uncommitted.yml)
mod config build style openrewrite edit "$PWD/no-star-imports.yml" --local .
mod config build style openrewrite show --local .

# rebuild (style must be in the LST, so a fresh mod build is required)
rm -rf .moderne/build
mod build .
mod run . --recipe=org.openrewrite.java.OrderImports

# observe: Target.java is unchanged
git diff src/main/java/com/example/Target.java
```

Expected: no diff. The five explicit imports are preserved.

## Verifying the LST actually contains the configured style

```bash
jar tf .moderne/build/*/*-ast.jar | grep styles
```

- **Without** the configured style you should only see `*.Autodetect.styles`.
- **With** the configured style you should also see `abc.no-star-imports.styles`
  in each build step's sub-directory (e.g. `1-gradlebuildstep/` and
  `2-resourcebuildstep/`).

## Cleanup

```bash
mod config build style openrewrite delete --local .
rm -rf .moderne
git checkout -- src/main/java/com/example/Target.java
```
