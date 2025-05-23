"""empty message

Revision ID: 42accf238bd3
Revises: 1c60f18a5d32
Create Date: 2020-06-03 22:42:15.558417

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '42accf238bd3'
down_revision = '1c60f18a5d32'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.alter_column('todos', 'list_id',
               existing_type=sa.INTEGER(),
               nullable=False)
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.alter_column('todos', 'list_id',
               existing_type=sa.INTEGER(),
               nullable=True)
    # ### end Alembic commands ###
