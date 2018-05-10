#前端js使用说明
```
cordova plugin add https://github.com/matrix-yang/TransformVoiceToText.git
```
```javascript
  cordova.plugins.TransformVoiceToText.transform("cantonese",function (msg) {
            alert("success"+msg);
        },function (err) {
            alert("err"+err);
        });
```      
第一个参数为 方言类型 可选参数为
普通话：mandarin
粤 语：cantonese
四川话：lmz
必须传入其中一种，否则报错
