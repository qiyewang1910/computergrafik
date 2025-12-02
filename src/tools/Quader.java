package tools;

import tools.Color;
import tools.Ebene;
import tools.Group;
import tools.Mat44;

public class Quader extends Group {
    
    public Quader(double seite, Color color) {
        // 6个正方形面，边长=seite
        double half = seite / 2.0;

        // 上面 (Top): 法线 (0, 1, 0)  不需要旋转，只需沿 +Y 平移
        addFace(new Ebene(seite, true, color), 0, half,0, 0, 0, 0);

        // 下面 (Bottom): 法线 (0, -1, 0)  绕X旋转 180度 (或者 Z 180)，然后沿 -Y 平移
        addFace(new Ebene(seite, true, color), 0, -half,0, Math.PI,0,0);

        // 前面 (Front): 法线 (0, 0, -1) [假设 -Z 为前]   原始法线 Y，绕 X 旋转 -90度 -> -Z
        addFace(new Ebene(seite, true, color),  0, 0, -half, -Math.PI/2, 0,0);
        
        // 后面 (Back): 法线 (0, 0, 1)    绕 X 旋转 90度 -> +Z
        addFace(new Ebene(seite, true, color), 0, 0, half, Math.PI/2, 0,0);
       
        // 右面 (Right): 法线 (1, 0, 0).  原始法线 Y，绕 Z 旋转 -90度 -> X
        addFace(new Ebene(seite, true, color), half, 0, 0, 0, 0, -Math.PI/2);

        // 左面 (Left): 法线 (-1, 0, 0).   原始法线 Y，绕 Z 旋转 90度 -> -X
        addFace(new Ebene(seite, true, color), -half, 0, 0, 0, 0, Math.PI/2);
    }


    // 辅助方法：添加单个面（先旋转对齐方向，再平移到位）
    private void addFace(Ebene ebene, double tx, double ty, double tz, 
                            double rx, double ry, double rz) {
        Group face = new Group();

        // 1. 计算旋转矩阵 (组合 X, Y, Z 旋转)
        //  顺序可能依据你的 Mat44 实现有所不同，通常分开乘比较稳妥
        Mat44 rot = Mat44.rotateX(rx)
                         .multiply(Mat44.rotateY(ry))
                         .multiply(Mat44.rotateZ(rz));

        // 2. 计算平移矩阵
        Mat44 trans = Mat44.translate(tx, ty, tz);

        // 3. 组合变换：先旋转(局部朝向)，再平移(推到表面位置)
        // transform = Translate * Rotate
        Mat44 finalMat = trans.multiply(rot);

        face.setTransform(finalMat);
        face.addChild(ebene);
        this.addChild(face);
    }

    // 默认6x6x6正方体s
    public Quader() {
        this(6, new Color(0.1, 0.5, 0.9));
    }

    // 兼容原有长方体构造方法
    public Quader(double l, double b, double h, Color color) {
        this(Math.min(l, Math.min(b, h)), color); // 强制正方体
    }
}