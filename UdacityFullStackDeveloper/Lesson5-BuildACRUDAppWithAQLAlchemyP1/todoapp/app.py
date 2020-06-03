import sys
from flask import Flask, render_template, request, redirect, url_for, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate

"""
https://knowledge.udacity.com/questions/187503
"""

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:password@localhost:5432/todoapp'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

migrate = Migrate(app, db)


class Todo(db.Model):
    __tablename__ = 'todos'
    id = db.Column(db.Integer, primary_key=True)
    description = db.Column(db.String(), nullable=False)
    completed = db.Column(db.Boolean, nullable=False, default=False)

    def __repr__(self):
        return f'<Todo {self.id} {self.description}>'


@app.route('/todos/create', methods=['GET'])
def create_todo():
    # description = request.get_json()['description']
    # todo = Todo(description=description)
    # db.session.add(todo)
    # db.session.commit()
    # return jsonify({
    #     'description': todo.description
    # })
    error = False
    # initialize the todo_object
    todo_object = {}
    try:
        description = request.get_json()['description']
        todo = Todo(description=description)
        db.session.add(todo)
        db.session.commit()
        # we have access to the todo, so we can save it to the todo_object here
        todo_object['description'] = todo.description
    except:
        error = True
        db.session.rollback()
        print(sys.exc_info)
    finally:
        db.session.close()
    if not error:
        # since todo_object already looks has the description key, we can jsonify the whole thing
        return jsonify(todo_object)


@app.route('/')
def index():
    return render_template('index.html', data=Todo.query.all())
