package tools;

/**
 * Represents a 4x4 matrix for 3D transformations.
 */
public final class Mat44 {

    /** The identity matrix. */
    public static final Mat44 identity;  //静态常量 单位矩阵（无变换）

    static {
        identity = new Mat44();  //初始化为单位矩阵 静态代码块
    }

    /**
     * Returns a string representation of the matrix.
     *
     * @return A string representation of the matrix, with each row on a new line.
     */
    @Override
    public String toString() {
        String s = "";
        for (int r = 0; r < 4; r++) {
            s += String.format("(% .2f % .2f % .2f % .2f)\n", get(0, r), get(1, r), get(2, r), get(3, r));
        }
        return s;
    }

    /**
     * Checks if this matrix is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mat44))
            return false;
        if (o == this)
            return true;
        Mat44 m = (Mat44) o;
        for (int i = 0; i != 16; i++)
            if (values[i] != m.values[i])
                return false;
        return true;
    }

    /**
     * Constructs a new Mat44 instance and initializes it as an identity matrix.
     */
    Mat44() {
        makeIdentity();
    }

    /**
     * Returns the internal array of matrix values.
     *
     * @return The array of matrix values.
     */
    double[] values() {
        return values;
    }

    void setValues(double[] values) {
        this.values = values;
    }

    /**
     * Gets the value at the specified column and row of the matrix.
     *
     * @param c The column index (0-3).
     * @param r The row index (0-3).
     * @return The value at the specified position.
     */
    double get(int c, int r) {
        return values[4 * c + r];    // 列优先存储 4*列 + 行
    }

    /**
     * Sets the value at the specified column and row of the matrix.
     *
     * @param c The column index (0-3).
     * @param r The row index (0-3).
     * @param v The value to set.
     */
    void set(int c, int r, double v) {
        values[4 * c + r] = v;
    }

    /**
     * Resets this matrix to the identity matrix.
     *
     * @return This matrix, reset to the identity matrix.
     */
    Mat44 makeIdentity() {
        values = new double[16];   // 初始化16个元素的数组
        set(0, 0, 1.0f);   // 列0，行0 = 1
        set(1, 1, 1.0f);   // 列1，行1 = 1
        set(2, 2, 1.0f);
        set(3, 3, 1.0f);
        return this;
    }

    /**
     * Multiplies this matrix with another matrix.
     *
     * @param m The matrix to multiply with.
     * @return A new Mat44 instance representing the result of the multiplication.
     */
    public Mat44 multiply(Mat44 m) {
        Mat44 n = new Mat44();   // 新建矩阵存储结果
        // 硬编码的4x4矩阵乘法（逐列逐行计算）
        // 结果矩阵的 (列c, 行r) = 本矩阵的列c · 传入矩阵的行r（点积）
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 3] = v;
            }
        }
        return n;
    }

    public Mat44 invert() {
        Mat44 inv = new Mat44(); // 初始化为单位矩阵
    
        // --- 1. 处理旋转部分 ---
        // 原来的代码只能处理Y轴，现在我们要处理任意旋转。
        // 旋转矩阵的逆 = 转置 (Transpose)，也就是把 行 变成 列。
        
        // 读取原矩阵的“基向量”（这代表了X, Y, Z轴的方向）
        // 第一列 (X轴方向)
        double r00 = get(0, 0); 
        double r10 = get(0, 1); 
        double r20 = get(0, 2);
        
        // 第二列 (Y轴方向)
        double r01 = get(1, 0); 
        double r11 = get(1, 1); 
        double r21 = get(1, 2);
        
        // 第三列 (Z轴方向)
        double r02 = get(2, 0); 
        double r12 = get(2, 1); 
        double r22 = get(2, 2);
    
        // 设置到逆矩阵中：行列互换 (注意看 set 的坐标变化)
        // 例如：原矩阵的 (1,0) 放到新矩阵的 (0,1)
        inv.set(0, 0, r00); inv.set(1, 0, r10); inv.set(2, 0, r20);
        inv.set(0, 1, r01); inv.set(1, 1, r11); inv.set(2, 1, r21);
        inv.set(0, 2, r02); inv.set(1, 2, r12); inv.set(2, 2, r22);

        // --- 2. 处理平移部分 ---
        // 原矩阵的平移量
        double tx = get(3, 0);
        double ty = get(3, 1);
        double tz = get(3, 2);

        // 逆平移不仅仅是取反 (-tx)，因为坐标轴旋转了，
        // 我们需要计算： - (旋转部分的转置 * 平移量)
        // 这其实就是点积运算：
        double invTx = -(r00 * tx + r10 * ty + r20 * tz);
        double invTy = -(r01 * tx + r11 * ty + r21 * tz);
        double invTz = -(r02 * tx + r12 * ty + r22 * tz);

        inv.set(3, 0, invTx);
        inv.set(3, 1, invTy);
        inv.set(3, 2, invTz);
    
        // 保持齐次坐标的 1
        inv.set(3, 3, 1.0);
    
        return inv;
    }


    /**
     * 创建缩放矩阵（列主序）
     */
    public static Mat44 scale(double sx, double sy, double sz) {
        Mat44 mat = new Mat44();
        // 缩放矩阵：对角线为缩放因子，其余为0
        mat.set(0, 0, sx);
        mat.set(1, 1, sy);
        mat.set(2, 2, sz);
        mat.set(3, 3, 1.0);
        return mat;
    }

    /**
     * 创建绕Y轴旋转矩阵（弧度制）
     */
    public static Mat44 rotateY(double angle) {
        Mat44 mat = new Mat44();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // 绕Y轴旋转矩阵（列主序）
        mat.set(0, 0, cos);
        mat.set(0, 2, -sin);
        mat.set(2, 0, sin);
        mat.set(2, 2, cos);
        mat.set(1, 1, 1.0);
        mat.set(3, 3, 1.0);
        return mat;
    }

    /**
     * 创建绕X轴旋转矩阵（弧度制）
     */
    public static Mat44 rotateX(double angle) {
        Mat44 mat = new Mat44();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // 绕X轴旋转矩阵（列主序）
        mat.set(1, 1, cos);
        mat.set(1, 2, sin);
        mat.set(2, 1, -sin);
        mat.set(2, 2, cos);
        mat.set(0, 0, 1.0);
        mat.set(3, 3, 1.0);
        return mat;
    }

    // 绕Z轴旋转矩阵 
    public static Mat44 rotateZ(double angle) {
        Mat44 mat = new Mat44();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // 绕Z轴旋转矩阵（列主序）
        mat.set(0, 0, cos);
        mat.set(0, 1, sin);
        mat.set(1, 0, -sin);
        mat.set(1, 1, cos);
        mat.set(2, 2, 1.0);
        mat.set(3, 3, 1.0);
        return mat;
    }

    /**
     * 创建平移矩阵（支持int/double参数）
     */
    public static Mat44 translate(double tx, double ty, double tz) {
        Mat44 mat = new Mat44();
        // 平移矩阵：最后一列是平移量，对角线为1
        mat.set(0, 0, 1.0);
        mat.set(1, 1, 1.0);
        mat.set(2, 2, 1.0);
        mat.set(3, 0, tx);
        mat.set(3, 1, ty);
        mat.set(3, 2, tz);
        mat.set(3, 3, 1.0);
        return mat;
    }

    // 兼容int参数的translate
    public static Mat44 translate(int tx, int ty, int tz) {
        return translate((double) tx, (double) ty, (double) tz);
    }



    /**
     * 用矩阵变换三维点（考虑平移分量）
     * 点的齐次坐标为 (x, y, z, 1)
     */
    public Vec3 multiplyPoint(Vec3 point) {
        // 列主序矩阵 × 点的计算（矩阵列 × 点的行向量）
        double x = point.x() * get(0, 0) + point.y() * get(0, 1) + point.z() * get(0, 2) + get(0, 3);
        double y = point.x() * get(1, 0) + point.y() * get(1, 1) + point.z() * get(1, 2) + get(1, 3);
        double z = point.x() * get(2, 0) + point.y() * get(2, 1) + point.z() * get(2, 2) + get(2, 3);
        return new Vec3(x, y, z);
    }

    /**
     * 用矩阵变换方向向量（不考虑平移分量）
     * 方向向量的齐次坐标为 (x, y, z, 0)
     */
    public Vec3 multiplyDirection(Vec3 dir) {
        // 列主序矩阵 × 方向向量（忽略平移分量）
        double x = dir.x() * get(0, 0) + dir.y() * get(0, 1) + dir.z() * get(0, 2);
        double y = dir.x() * get(1, 0) + dir.y() * get(1, 1) + dir.z() * get(1, 2);
        double z = dir.x() * get(2, 0) + dir.y() * get(2, 1) + dir.z() * get(2, 2);
        return new Vec3(x, y, z);
    }



    private double[] values;
}


