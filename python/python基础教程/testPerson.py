from Person import Person

per=Person()
per.set_name('limeng')
per.greet()
print(per.get_nickname())

greetfun=per.greet
greetfun()

print(per.get_salary())
