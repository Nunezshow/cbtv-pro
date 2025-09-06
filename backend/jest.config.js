diff --git a//dev/null b/backend/jest.config.js
index 0000000000000000000000000000000000000000..7aa6f5963175b4c65d81ad3f705cfe95e3e2c4f9 100644
--- a//dev/null
+++ b/backend/jest.config.js
@@ -0,0 +1,5 @@
+module.exports = {
+  preset: 'ts-jest',
+  testEnvironment: 'node',
+  testMatch: ['**/*.test.ts']
+};
