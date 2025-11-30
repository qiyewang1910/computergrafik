package tools;

import tools.Color;
import tools.Ebene;
import tools.Group;
import tools.Mat44;


public class Quader extends Group {
    
    public Quader(double seite, Color color) {
        // 6个正方形面，边长=seite
        double halfSeite = seite / 2.0;

        // 前面（Z=seite/2）
        addFace(new Ebene(seite, true, color), 0, 0, halfSeite, 0, 0, 0);
        // 后面（Z=-seite/2，旋转180°）
        addFace(new Ebene(seite, true, color), 0, 0, -halfSeite, 0, Math.PI,0);
        // 左面（X=-seite/2，旋转-90°）
        addFace(new Ebene(seite, true, color), -halfSeite, 0, 0, 0, -Math.PI/2, 0);
        // 右面（X=seite/2，旋转90°）
        addFace(new Ebene(seite, true, color), halfSeite, 0, 0, 0, Math.PI/2, 0);
        // 上面（Y=seite/2，旋转-90°）
        addFace(new Ebene(seite, true, color), 0, halfSeite, 0, -Math.PI/2, 0, 0);
        // 下面（Y=-seite/2，旋转90°）
        addFace(new Ebene(seite, true, color), 0, -halfSeite, 0, Math.PI/2, 0, 0);
    }

    // 辅助方法：添加单个面（平移+旋转）
    private void addFace(Ebene ebene, double tx, double ty, double tz, double rx, double ry, double rz) {
        Group face = new Group();

        Mat44 trans = Mat44.translate(tx, ty, tz)
                           .multiply(Mat44.rotateX(rx))
                           .multiply(Mat44.rotateY(ry));
        face.setTransform(trans);
        face.addChild(ebene);
        this.addChild(face);
    }

    // 默认6x6x6正方体
    public Quader() {
        this(6, new Color(0.1, 0.5, 0.9));
    }

    // 兼容原有长方体构造方法
    public Quader(double l, double b, double h, Color color) {
        this(Math.min(l, Math.min(b, h)), color); // 强制正方体
    }
}