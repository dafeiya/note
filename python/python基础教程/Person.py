class Person:
    nickname="dafeiya"
    __salary='20K'
    def set_name(self,name):
        self.name=name

    def get_name(self):
        return self.name

    def greet(self):
        print("hello, {0}".format(self.name))
    
    def get_nickname(self):
        return self.nickname

    def __get_salary(self):
        return self.__salary

    def get_salary(self):
        return self.__get_salary()


#1.class规则：
#对 Person类型的变量在调用 set_name 和 greet 时, 实例都会作为第一个参数自动传递给函数。我将这个参数命名为 self ,这非常贴切。实际上,可以随便给这个参数命名,但鉴于它总是指向对象本身,因此习惯上将其命名为 self
#self所指代的形参在class定义函数时必须显示定义，但通过实例对象调用函数时不可以传入实参。因为已自动传入

#2.私有方法或属性：
#要让方法或属性成为私有的(不能从外部访问) ,只需让其名称以两个下划线'__'打头即可。私有方法或属性只能在class内部调用，无法在外部调用。虽然可以通过一些其它途径可以在外部调用时获取到私有方法或属性，但不建议这样做，某种程度上这是种约定





