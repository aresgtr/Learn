package charactor;

public class Hero {

    String name;

    float hp;

    float armor;

    int moveSpeed;

    //获取护甲值
    float getArmor(){
        return armor;
    }

    //坑队友
    void keng(){
        System.out.println("坑队友！");
    }

    //增加移动速度
    void addSpeed(int speed){
        //在原来的基础上增加移动速度
        moveSpeed = moveSpeed + speed;
    }
}
