package tools;


public class Quader extends Group {

    /**
     * 创建一个长方体 (Cuboid)
     * @param xSize X轴方向的长度 (宽)
     * @param ySize Y轴方向的长度 (高)
     * @param zSize Z轴方向的长度 (深)
     * @param color 颜色
     */
    
    public Quader(double xSize, double ySize, double zSize, Color color) {
        // 6个正方形面，边长=seite
        double hx = xSize / 2.0;
        double hy = ySize / 2.0;
        double hz = zSize / 2.0;

        // 核心技巧：使用一个边长为 1.0 的“单位平面”作为基础
        // 然后通过缩放 (Scale) 把它变成任意大小的长方形
        // 注意：这里每次都 new 一个新的 Ebene，确保独立性
        
        // 1. 上面 (Top): 法线 +Y
        // 尺寸：X * Z。位移：+Y半高
        addFace(new Ebene(1.0, true, color), 0, hy, 0, 0, 0, 0, xSize, 1, zSize);

        // 2. 下面 (Bottom): 法线 -Y
        // 尺寸：X * Z。位移：-Y半高。旋转：绕X 180度
        addFace(new Ebene(1.0, true, color), 0, -hy, 0, Math.PI, 0, 0, xSize, 1, zSize);

        // 3. 前面 (Front): 法线 +Z
        // 尺寸：X * Y。位移：+Z半深。旋转：绕X 90度 (让原本躺着的平面立起来)
        // 注意：旋转后，平面的“深度Z”对应了世界的“高度Y”，所以缩放Z分量设为 ySize
        addFace(new Ebene(1.0, true, color), 0, 0, hz, Math.PI/2, 0, 0, xSize, 1, ySize);

        // 4. 后面 (Back): 法线 -Z
        // 尺寸：X * Y。位移：-Z半深。旋转：绕X -90度
        addFace(new Ebene(1.0, true, color), 0, 0, -hz, -Math.PI/2, 0, 0, xSize, 1, ySize);

        // 5. 右面 (Right): 法线 +X
        // 尺寸：Z * Y (侧面)。位移：+X半宽。旋转：绕Z -90度
        // 旋转后：平面的X轴指向世界-Y，平面的Z轴指向世界+Z？
        // 这里的缩放逻辑：平面的X轴(sx)变成了高度方向，平面的Z轴(sz)变成了深度方向
        addFace(new Ebene(1.0, true, color), hx, 0, 0, 0, 0, -Math.PI/2, ySize, 1, zSize);

        // 6. 左面 (Left): 法线 -X
        // 尺寸：Z * Y。位移：-X半宽。旋转：绕Z 90度
        addFace(new Ebene(1.0, true, color), -hx, 0, 0, 0, 0, Math.PI/2, ySize, 1, zSize);
    }


    // 辅助方法：添加单个面（先旋转对齐方向，再平移到位）
    private void addFace(Ebene ebene, double tx, double ty, double tz, 
                            double rx, double ry, double rz,
                            double sx, double sy, double sz) {
        Group face = new Group();

        // 1. 缩放矩阵
        Mat44 scale = Mat44.scale(sx, sy, sz);
        
        // 2. 旋转矩阵（绕X、Y、Z依次旋转）
        Mat44 rot = Mat44.rotateX(rx)
                        .multiply(Mat44.rotateY(ry))
                        .multiply(Mat44.rotateZ(rz));

        // 3. 平移矩阵
        Mat44 trans = Mat44.translate(tx, ty, tz);

        // 4. 组合变换：先缩放，再旋转，最后平移 T*R*S
        Mat44 finalMat = trans.multiply(rot).multiply(scale);

        face.setTransform(finalMat);
        face.addChild(ebene);
        this.addChild(face);
    }

    // 正方体
    public Quader(double seite, Color color) {
        this(seite, seite, seite, color); 
    }

    // 兼容原有长方体构造方法
    public Quader(double seite, Color color, double alpha) {
        this(seite, seite, seite, color); 
    }
}