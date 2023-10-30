// const { createProxyMiddleware } = require("http-proxy-middleware");

// module.exports = function(app) {
//   app.use("/admin-service",
//     createProxyMiddleware(
//       {
//         target: "http://localhost:8000",
//         changeOrigin: true,
//       }
//     )
//   );
//   app.use("/user-service",
//   createProxyMiddleware(
//     {
//       target: "http://localhost:8000",
//       changeOrigin: true,
//     }
//   )
// );
// };