package tools;

import tools.Ebene;

public class Quader extends Group {
    // 正方体构造方法（边长+颜色）
    public Quader(double seite, Color color) {
        // 6个正方形面，边长=seite
        Ebene seiteQuad = new Ebene(seite, true, color);

        // 前面（Z=seite/2）
        addFace(seiteQuad, 0, 0, seite/2, 0, 0, 0);
        // 后面（Z=-seite/2，旋转180°）
        addFace(seiteQuad, 0, 0, -seite/2, 0, 0, Math.PI);
        // 左面（X=-seite/2，旋转-90°）
        addFace(seiteQuad, -seite/2, 0, 0, 0, -Math.PI/2, 0);
        // 右面（X=seite/2，旋转90°）
        addFace(seiteQuad, seite/2, 0, 0, 0, Math.PI/2, 0);
        // 上面（Y=seite/2，旋转-90°）
        addFace(seiteQuad, 0, seite/2, 0, -Math.PI/2, 0, 0);
        // 下面（Y=-seite/2，旋转90°）
        addFace(seiteQuad, 0, -seite/2, 0, Math.PI/2, 0, 0);
    }

    // 辅助方法：添加单个面（平移+旋转）
    private void addFace(Ebene seite, double tx, double ty, double tz, double rx, double ry, double rz) {
        Group face = new Group();
        Mat44 trans = Mat44.rotateX(rx)
                           .multiply(Mat44.rotateX(rx))
                           .multiply(Mat44.rotateY(ry));
        face.setTransform(trans);
        face.addChild(seite);
        this.addChild(face);
    }

    // 默认6x6x6蓝色正方体
    public Quader() {
        this(6, new Color(0.1, 0.5, 0.9));
    }

    // 兼容原有长方体构造方法
    public Quader(double l, double b, double h, Color color) {
        this(Math.min(l, Math.min(b, h)), color); // 强制正方体
    }
}