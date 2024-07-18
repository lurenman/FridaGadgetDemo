function main() {
    //使用java平台
    Java.perform(
        function () {
            //hook android id 方法
            var SettingsSecure = Java.use('android.provider.Settings$Secure');
            SettingsSecure.getString.implementation = function (resolver, name) {
                if (name === "android_id") {
                   return "123456789";
                }
                // 调用原始方法
                return this.getString(resolver, name);
            };
        }
    );
}
setImmediate(main)