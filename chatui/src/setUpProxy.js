const { createProxyMiddleware } = require('http-proxy-middleware');
module.exports = function (app) {
    app.use(
        '/users-ws',
        createProxyMiddleware({
            target: 'http://localhost:8082',
            changeOrigin: true,
        }),
       
    );
};