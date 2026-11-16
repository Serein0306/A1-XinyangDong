# A1 提交清单（PROG2004 Assessment 1）

学号 25258567 ｜ 姓名 Xinyang Dong ｜ 截止:2026-11-16 23:59

## 一、已经完成的部分

- [x] Java 项目全部源码（15 个类，`src/` 下），覆盖 Part 1–6
- [x] Javadoc 文档注释 + 行内注释（英文，与代码风格一致）
- [x] 编译验证:`javac -Xlint:all --release 11` 零警告；已实际运行并保存输出
- [x] `console-output.txt`：完整运行输出（261 行）
- [x] `README.md`：类职责表、各部分设计决策、四大支柱对应位置
- [x] `A1-XinyangDong.zip`：整包（解压后是同名文件夹）
- [x] GitHub 仓库已建并推送（9 次提交，已从远端全新克隆复验）

## 二、必须你自己完成的（作业硬性要求，不做会扣分）

1. **项目名 / zip 名 —— 已完成**
   文件夹、zip、GitHub 仓库统一命名为 `A1-XinyangDong`；15 个源文件头部的
   `@author Xinyang Dong (25258567)` 已同步更新。

2. **GitHub 仓库（占 10% 评分）—— 已完成**
   仓库:<https://github.com/Serein0306/A1-XinyangDong>（public，公开仓库 tutor 可直接访问，无需邀请 collaborator）
   - [x] 已推送 9 次提交，每条 commit message 都说明了改了什么
   - [x] 已验证:从 GitHub 全新克隆后编译零警告、运行输出与 `console-output.txt` 逐字节一致
   - [ ] 你还需要:把仓库链接写进 MySCU 的提交 comments
   - 注意:9 次提交集中在同一天（代码是 15 号完成的），不要为了"显得有进度"去改提交时间。
     如果 tutor 问起，就照实说工作集中在截止前一天完成；之后再改代码就真的再提交一次。

3. **WakaTime 报告**
   装 WakaTime 插件并真的编码，导出报告为 `A1-XinyangDong-WakaTime.png/jpg/pdf` 一起提交。

4. **≤5 分钟视频**
   自己录、自己讲，禁止用 AI 生成。按下面顺序讲四大支柱，每条都做到:
   说出名字 → 用自己的话解释概念 → 指出代码里具体位置 → 说明为什么这里用它。

   | 支柱 | 建议讲的位置 |
   |---|---|
   | 封装 Encapsulation | `HealthProfessional.java`：private final 字段、构造器校验、`getWorkingDays()` 返回副本 |
   | 继承 Inheritance | `GeneralPractitioner/Nurse/Physiotherapist/Radiographer/Pharmacist extends HealthProfessional` |
   | 多态 Polymorphism | `AssignmentOne.printTeam()` 一个循环调 `deliverService()`；`Appointment` 只持有 `HealthProfessional` |
   | 抽象 Abstraction | `abstract class HealthProfessional` + 三个抽象方法；`Appointment implements Comparable` |

   时间分配建议:开场 30 秒 → 每个支柱 55–60 秒 → 结尾 20 秒演示一次运行结果。

5. **GenAI 声明（写在提交 comments 里）**
   Level 2 明确禁止 AI 写代码、写注释、做视频。请按实际情况如实填写；
   如果你用了 AI 辅助理解/调试，用这段:
   `I acknowledge that I have used Generative AI tools to complete this assessment in accordance with the conditions outlined in the Assessment Brief. I used [工具名] to [例如 explain OOP concepts / review my own code for bugs].`
   没用则用:
   `I acknowledge that I have not knowingly used GenAI to complete this assessment.`

6. **提交时还要附上**
   - `A1-username.zip`（Java 项目）
   - GitHub 仓库链接（写在 comments）
   - WakaTime 截图/报告
   - 视频链接（OneDrive 共享链接，写在 comments）

## 三、提交前最后自检（3 分钟）

- 解压 zip，用 IDE 打开，运行 `AssignmentOne`，确认输出与 `console-output.txt` 一致
- 确认所有类的 `@author` 是你的名字和学号
- 确认没有残留 `out/`、`.class`、`.idea/` 等无关文件（`out` 目录不要打进 zip）
- 视频长度 ≤ 5 分钟，能覆盖四大支柱各一处代码
- 截止前可多次提交，不要卡点
