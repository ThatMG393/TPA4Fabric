# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v0.0.4+release.1] - 2024-11-08
### New Features
- [`35fa87c`](https://github.com/ThatMG393/TPA4Fabric/commit/35fa87ca7fe7fbed92f1487bb894f59e5a541e2e) - proper CommandResult with optional data *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`157c1cd`](https://github.com/ThatMG393/TPA4Fabric/commit/157c1cddfb603adf1a72c8e44d4b53439f9c9fb6) - include remaining time to wait on cooldown message *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`7381c12`](https://github.com/ThatMG393/TPA4Fabric/commit/7381c1203c7d43657474f431eda5c567bf946b4b) - load previous position after teleport *(commit by [@ThatMG393](https://github.com/ThatMG393))*

### Bug Fixes
- [`5f9e9f5`](https://github.com/ThatMG393/TPA4Fabric/commit/5f9e9f504b02628dea4e6d2dc0dd72c4e8e5f00b) - request still being accepted even if you deny it *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`e349b2f`](https://github.com/ThatMG393/TPA4Fabric/commit/e349b2feb7c3373954af5ccc8f6299123f894a1b) - remove duplicate deny message logic *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`ff9390c`](https://github.com/ThatMG393/TPA4Fabric/commit/ff9390c8782362c21d5b8792f02986d512740bcf) - initialize `running` properly *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`54f35c1`](https://github.com/ThatMG393/TPA4Fabric/commit/54f35c100db1e5d9df04bb09e1d090a2695c3099) - load chunks on the correct server world *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`31860eb`](https://github.com/ThatMG393/TPA4Fabric/commit/31860ebb9f03c9807a7e8bb276d1dc88da5ae717) - wrong type casting on `TPAManager#tpa()` *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`988beae`](https://github.com/ThatMG393/TPA4Fabric/commit/988beaea67785f0d503b4d997b53dc0f199e0d26) - make `ChunkTicketType``after_teleport` constant *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`14aeea2`](https://github.com/ThatMG393/TPA4Fabric/commit/14aeea23fdda786c19cc78f113132dc90044a59c) - also set previous chunkpos when invoking tpaback *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`b713772`](https://github.com/ThatMG393/TPA4Fabric/commit/b713772f0b52f8ebe71b2caed0e52c7b64368cd6) - un-async player teleportation (fixes a C2ME issue) *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`19fe18a`](https://github.com/ThatMG393/TPA4Fabric/commit/19fe18ae34b42aa1550bfbfbc737aa4a49178091) - fix the empty world when joining *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`ea9851e`](https://github.com/ThatMG393/TPA4Fabric/commit/ea9851e9f7231fc7fa77ed6e75eb078d00edd0fd) - just check for UUID *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`6635a1c`](https://github.com/ThatMG393/TPA4Fabric/commit/6635a1cf0c60c31922957f5398b817a1b9f915c1) - wrong check fix *(commit by [@ThatMG393](https://github.com/ThatMG393))*

### Tests
- [`bba8d25`](https://github.com/ThatMG393/TPA4Fabric/commit/bba8d25fd20322e00c3cc8fa936718ad3fbf06e3) - head scratching ++ *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`a2881bb`](https://github.com/ThatMG393/TPA4Fabric/commit/a2881bbc26a5187688415d609b2d02ff963e265e) - more head scratching *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`4fc2eb3`](https://github.com/ThatMG393/TPA4Fabric/commit/4fc2eb36f342f740d3adfb0e95fd70401bc2426a) - debug logsss *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`14104fb`](https://github.com/ThatMG393/TPA4Fabric/commit/14104fb68858670aaf5dd1326ed81593a61cd413) - idek what i did l *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`1584316`](https://github.com/ThatMG393/TPA4Fabric/commit/1584316c2880519ee1dfaf5e6f5d1052252f4d90) - more debug *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`86c343f`](https://github.com/ThatMG393/TPA4Fabric/commit/86c343f8575df8b9d9fd6ddf70b621f4f4cc8caa) - add debug log *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`7305cb7`](https://github.com/ThatMG393/TPA4Fabric/commit/7305cb73a9ba648b6dd05749b65fd83935b7fe5c) - try to re-set `ServerPlayerEntity` reference after player respawn *(commit by [@ThatMG393](https://github.com/ThatMG393))*


## [v0.0.3+release.2] - 2024-10-24
### Other Changes
- [`eee14bc`](https://github.com/ThatMG393/TPA4Fabric/commit/eee14bc9979d70cc680dda580d57fbd7b7c2874c) - Fix double "Teleporting in Xs..." *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`d6cec22`](https://github.com/ThatMG393/TPA4Fabric/commit/d6cec229ef7aebb746d4a91dcfbc77a969b192f6) - Remove unused imports *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`ab7a799`](https://github.com/ThatMG393/TPA4Fabric/commit/ab7a79981d8a27a7f20fc4fcfaf89aba6bf3b035) - Merge branch 'master' of https://github.com/ThatMG393/TPA4Fabric *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`02d198b`](https://github.com/ThatMG393/TPA4Fabric/commit/02d198b9c3093d7f82d9f449205b8e148f6265b4) - Add a little bit of formatting *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`de9b63a`](https://github.com/ThatMG393/TPA4Fabric/commit/de9b63ad22fe3da1f460306ee7bbcb555a609eee) - Update CI thingies, upload built artifacts from Java 17 *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`3387940`](https://github.com/ThatMG393/TPA4Fabric/commit/33879402c3e623d8d1193888cb90cc950fd6ad9b) - Remove useless `TPAPlayerWrapper#sendMessage(Text)` on `TPARequest#accept()` *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`39b66ef`](https://github.com/ThatMG393/TPA4Fabric/commit/39b66efbead73792bbbd6b7f188d9fcc74bff644) - Dont check for Y axis when teleporting, also increase frequency of position checking *(commit by [@ThatMG393](https://github.com/ThatMG393))*


## [v0.0.3+release.1] - 2024-10-19
### :flying_saucer: Other Changes
- [`afebafc`](https://github.com/ThatMG393/TPA4Fabric/commit/afebafc812fcbeab53b1d96375e1572c3d382d1b) - Hopefully fixes future release builds *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`522a626`](https://github.com/ThatMG393/TPA4Fabric/commit/522a62656af28e5c1ced1ff29f4173602ce913b2) - Properly remove TPARequest in the HashMap *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`299664b`](https://github.com/ThatMG393/TPA4Fabric/commit/299664b2caf8a3b65bb721f3aae2f16ea61ce890) - Fix a very stupid typo *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`a823cb6`](https://github.com/ThatMG393/TPA4Fabric/commit/a823cb6986b25538c30218dfe627ea48b8d0f106) - Default always allow TPA request *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`fbdd9a4`](https://github.com/ThatMG393/TPA4Fabric/commit/fbdd9a43a44bb0e2d7c3dbe34a4882af199007da) - Initial refactor *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`c09da5c`](https://github.com/ThatMG393/TPA4Fabric/commit/c09da5ce3207326852eef849bf3fe1f69075efcc) - Forgot to remove useless class *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`8595bce`](https://github.com/ThatMG393/TPA4Fabric/commit/8595bcece208bd33bcbf6b5a0b104175db66d395) - Almost forgot to fix this *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`f75ff86`](https://github.com/ThatMG393/TPA4Fabric/commit/f75ff86230908d6245999d758a80f39c38773812) - Increase version range reaching 1.21.x *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`7db378d`](https://github.com/ThatMG393/TPA4Fabric/commit/7db378dbe6189b120645ce805786af6d8fb2ab17) - Geyser compat *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`3baa9c9`](https://github.com/ThatMG393/TPA4Fabric/commit/3baa9c9afbec9f3a3b9341fd591ca1cfc972dbaa) - Early return if ServerPlayerEntity is null *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`7542a7c`](https://github.com/ThatMG393/TPA4Fabric/commit/7542a7c8421002132ab02def513ab457f7d71314) - Fix TPA Requests consumption *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`87939e0`](https://github.com/ThatMG393/TPA4Fabric/commit/87939e0e408349bb8fb29ae50fa16ffe291f4655) - Merge branch 'master' of https://github.com/ThatMG393/TPA4Fabric *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`4de1dc7`](https://github.com/ThatMG393/TPA4Fabric/commit/4de1dc7f54fc07fbe858710b777f50b6176ac895) - A little bit of refactor *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`9618bab`](https://github.com/ThatMG393/TPA4Fabric/commit/9618babc3f32c4b10d19801656e926338fc61763) - Fix syntax *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`438472d`](https://github.com/ThatMG393/TPA4Fabric/commit/438472dab01edec772546165f6932d3121989b58) - Get first request on HashMap if no "player" is passed *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`43cecd9`](https://github.com/ThatMG393/TPA4Fabric/commit/43cecd95a2199346b46fd16b97c690fca5253ddf) - Cleanup some logic *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`0603277`](https://github.com/ThatMG393/TPA4Fabric/commit/0603277771fdfb87d097407259a578e8cc513996) - Use cooldown from config instead of being a fixed value *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`97c96f8`](https://github.com/ThatMG393/TPA4Fabric/commit/97c96f80a274ab42336b489d97afed3ebecc2587) - Add `hasExisingTPARequest` in order to stop TPA spam *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`3558b95`](https://github.com/ThatMG393/TPA4Fabric/commit/3558b953ecbba61890f63f8aaacb800ebd66edff) - Ignore command when you already have a existing TPA *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`9611d2b`](https://github.com/ThatMG393/TPA4Fabric/commit/9611d2b4b3007aacdc535b200e37a86a8b5185ea) - Minor fixes, bugfixes, and cleanup *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`151ac47`](https://github.com/ThatMG393/TPA4Fabric/commit/151ac47f82a63e1314a9daaf78883d943e9e3f36) - Initial config versioning *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`b0134b5`](https://github.com/ThatMG393/TPA4Fabric/commit/b0134b5c30ec79b55a6ca7986a55bdb76d625c14) - Another on-going rewrite, updated gradle, and debug logs every second when teleporting *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`72f48f5`](https://github.com/ThatMG393/TPA4Fabric/commit/72f48f540eded3cb5aecf452d52ab2d301eec7cf) - Fix CI *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`25b4b1a`](https://github.com/ThatMG393/TPA4Fabric/commit/25b4b1a3b55682ea01f8df8146d98b458d1c515d) - Re-set realPlayer when the player died (testing) *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`dd16b22`](https://github.com/ThatMG393/TPA4Fabric/commit/dd16b224f8bdd9b6e6afc56484e6091e1c5cceb0) - Added more debug infos *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`e541783`](https://github.com/ThatMG393/TPA4Fabric/commit/e541783f53fc7416e2bc441782540cf69115f459) - Moar debug info *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`d63b4fb`](https://github.com/ThatMG393/TPA4Fabric/commit/d63b4fbf722ef3d65564a0e606000f6e3d5f4fd0) - Prepare for new feature *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`b11f611`](https://github.com/ThatMG393/TPA4Fabric/commit/b11f611259dc4ddbdcdfd61485c83ddce8b9da6e) - Avoid re-creating TPAPlayerOld each server tick *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`d373214`](https://github.com/ThatMG393/TPA4Fabric/commit/d3732149dd323a8527ac1968d56aa3470cd5eb2f) - Fix occational NPE *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`6588eef`](https://github.com/ThatMG393/TPA4Fabric/commit/6588eef8a2af0ae60d1e6925b4eacfd3d252f799) - Rewrite *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`5cc99ff`](https://github.com/ThatMG393/TPA4Fabric/commit/5cc99ffbcc79a57dd678af11a85e8c8af94341ca) - Cleanup imports *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`645138d`](https://github.com/ThatMG393/TPA4Fabric/commit/645138d0524fb18bf1c6dba94cb890786d75728f) - Fix translation key typo and instantenuous request expiration *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`d8f25f7`](https://github.com/ThatMG393/TPA4Fabric/commit/d8f25f74d5adbadb9041c4151487a74713619103) - Update README.md *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`e1b92bc`](https://github.com/ThatMG393/TPA4Fabric/commit/e1b92bcc7f3ddeffbbadbdd476d5447dd7d8f0ec) - Fix spelling, fix `isAlive()` logic *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`feac6ad`](https://github.com/ThatMG393/TPA4Fabric/commit/feac6ad3ef414ccd3da023ea6b968f08ded90f0d) - Merge branch 'master' of https://github.com/ThatMG393/TPA4Fabric *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`846a990`](https://github.com/ThatMG393/TPA4Fabric/commit/846a990db6e4534c13e6f96acc29179581692124) - Fix MCTextUtils formatting, fix another typo *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`a734875`](https://github.com/ThatMG393/TPA4Fabric/commit/a7348754e09cf90e122e4960ba17a88cc5052263) - Fix `/tpaback` *(commit by [@ThatMG393](https://github.com/ThatMG393))*
- [`a46bc23`](https://github.com/ThatMG393/TPA4Fabric/commit/a46bc23e6ee436d288c9fa69ea3de2ce6fa79862) - Fix infinite cooldown bug *(commit by [@ThatMG393](https://github.com/ThatMG393))*

[v0.0.3+release.1]: https://github.com/ThatMG393/TPA4Fabric/compare/v0.0.2+release.1...v0.0.3+release.1
[v0.0.3+release.2]: https://github.com/ThatMG393/TPA4Fabric/compare/v0.0.3+release.1...v0.0.3+release.2
[v0.0.4+release.1]: https://github.com/ThatMG393/TPA4Fabric/compare/v0.0.3+release.2...v0.0.4+release.1
