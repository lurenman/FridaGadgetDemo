  function main()
  {
   //使用java平台
     Java.perform(
        function() {
            //获取java类
            var student=Java.use("com.example.fridagadgetdemo.Student");
            //hook Add方法(重写Add方法)
            student.Add.implementation=function(a,b)
            {
                //修改参数
                a=123;
                b=456;
                //调用原来的函数
                var res = this.Add(a,b);
                //输出结果
                console.log(a,b,res);
                return res;
            }
        }

     );
    }
  setImmediate(main)